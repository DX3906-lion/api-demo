package com.apidemo.script.service;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.script.dto.ImportConfirmFieldRequest;
import com.apidemo.script.dto.ImportConfirmRequest;
import com.apidemo.script.dto.ImportConfirmResponse;
import com.apidemo.script.dto.ImportConfirmStepRequest;
import com.apidemo.script.dto.ImportPreviewResponse;
import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.ScriptDetailResponse;
import com.apidemo.script.dto.ScriptFieldDefaultResponse;
import com.apidemo.script.dto.StepPayloadContentResponse;
import com.apidemo.script.dto.StepDefinitionResponse;
import com.apidemo.script.dto.StepRequestConfigResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {
        "DELETE FROM step_payload_content",
        "DELETE FROM step_request_config",
        "DELETE FROM case_field_value",
        "DELETE FROM case_data_set",
        "DELETE FROM script_field_default",
        "DELETE FROM field_config",
        "DELETE FROM step_definition",
        "DELETE FROM import_log",
        "DELETE FROM raw_import_file",
        "DELETE FROM script_version",
        "DELETE FROM script"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ImportConfirmServiceTests {

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private ScriptVersionService scriptVersionService;

    @Autowired
    private StepDefinitionService stepDefinitionService;

    @Autowired
    private FieldConfigService fieldConfigService;

    @Autowired
    private ImportService importService;

    @Autowired
    private StepRequestContentService stepRequestContentService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void draftVersionCanConfirmImportAndCreateStepRequestConfigPayloadFieldDefaultAndImportTrace() throws Exception {
        ScriptDetailResponse script = scriptService.createScript(scriptRequest("import draft"));
        ImportPreviewResponse preview = importService.preview(file("sample.har"), "HAR");

        ImportConfirmResponse response = importService.confirm(script.getId(), script.getCurrentVersionId(), confirmRequest(preview.getImportFileId()));
        List<StepDefinitionResponse> steps = stepDefinitionService.listSteps(script.getId(), script.getCurrentVersionId());
        List<ScriptFieldDefaultResponse> defaults = fieldConfigService.listDefaults(script.getId(), script.getCurrentVersionId(), null, null);
        StepRequestConfigResponse requestConfig = stepRequestContentService.getRequestConfig(steps.get(0).getId());
        List<StepPayloadContentResponse> payloads = stepRequestContentService.listPayloadContents(steps.get(0).getId());

        assertNotNull(preview.getImportFileId());
        assertEquals(Boolean.FALSE, preview.getFormalScriptDataCreated());
        assertEquals(preview.getImportFileId(), response.getImportFileId());
        assertEquals(Integer.valueOf(1), response.getImportedStepCount());
        assertEquals(Integer.valueOf(2), response.getImportedFieldCount());
        assertEquals(Integer.valueOf(1), response.getImportedDefaultCount());
        assertEquals(1, steps.size());
        assertEquals("POST", steps.get(0).getRequestMethod());
        assertEquals("POST", requestConfig.getMethod());
        assertEquals("http://localhost:8080/login", requestConfig.getUrlTemplate());
        assertFalse(payloads.stream().noneMatch(payload ->
                "REQUEST".equals(payload.getDirection())
                        && "HEADER".equals(payload.getLocation())
                        && "KEY_VALUE".equals(payload.getContentFormat())));
        assertFalse(payloads.stream().noneMatch(payload ->
                "REQUEST".equals(payload.getDirection())
                        && "BODY".equals(payload.getLocation())
                        && "{\"username\":\"demo-user\"}".equals(payload.getRawContent())));
        assertEquals(2, fieldConfigService.listFields(script.getId(), script.getCurrentVersionId(), null, null).size());
        assertFalse(defaults.isEmpty());
        assertEquals("demo-user", defaults.get(0).getDefaultValue());
        assertEquals(preview.getImportFileId(), jdbcTemplate.queryForObject(
                "SELECT raw_import_file_id FROM script_version WHERE id = ?", String.class, script.getCurrentVersionId()));
        assertEquals("CONFIRMED", jdbcTemplate.queryForObject(
                "SELECT status FROM raw_import_file WHERE id = ?", String.class, preview.getImportFileId()));
        assertEquals(Integer.valueOf(1), jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM import_log WHERE raw_import_file_id = ? AND stage = 'CONFIRM' AND status = 'SUCCESS'",
                Integer.class, preview.getImportFileId()));
    }

    @Test
    void publishedVersionCannotConfirmImport() throws Exception {
        ScriptDetailResponse script = scriptService.createScript(scriptRequest("import published"));
        ImportPreviewResponse preview = importService.preview(file("sample.har"), "HAR");
        scriptVersionService.publishVersion(script.getId(), script.getCurrentVersionId());

        BizException exception = assertThrows(BizException.class, () ->
                importService.confirm(script.getId(), script.getCurrentVersionId(), confirmRequest(preview.getImportFileId())));

        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), exception.getCode());
    }

    @Test
    void confirmWithoutImportFileIdShouldFail() {
        ScriptDetailResponse script = scriptService.createScript(scriptRequest("import missing file id"));

        BizException exception = assertThrows(BizException.class, () ->
                importService.confirm(script.getId(), script.getCurrentVersionId(), confirmRequest(null)));

        assertEquals(ErrorCode.PARAM_INVALID.getCode(), exception.getCode());
    }

    private ScriptCreateRequest scriptRequest(String name) {
        ScriptCreateRequest request = new ScriptCreateRequest();
        request.setName(name);
        request.setDescription(name + " description");
        return request;
    }

    private ImportConfirmRequest confirmRequest(String importFileId) {
        ImportConfirmStepRequest step = new ImportConfirmStepRequest();
        step.setName("Login Request");
        step.setStepType("HTTP");
        step.setRequestMethod("POST");
        step.setRequestUrl("http://localhost:8080/login");
        step.setRequestConfig("{\"headers\":[{\"name\":\"Content-Type\",\"value\":\"application/json\"}],\"queryString\":[],\"postData\":{\"mimeType\":\"application/json\",\"text\":\"{\\\"username\\\":\\\"demo-user\\\"}\"}}");
        step.setFields(Arrays.asList(field("REQUEST_BODY", "$.username", "username", "demo-user"),
                field("REQUEST_HEADER", "header.Content-Type", "Content-Type", null)));

        ImportConfirmRequest request = new ImportConfirmRequest();
        request.setImportFileId(importFileId);
        request.setImportType("HAR");
        request.setSteps(Arrays.asList(step));
        return request;
    }

    private ImportConfirmFieldRequest field(String scope, String path, String key, String defaultValue) {
        ImportConfirmFieldRequest request = new ImportConfirmFieldRequest();
        request.setFieldScope(scope);
        request.setFieldPath(path);
        request.setFieldKey(key);
        request.setStableFieldKey("Login_Request." + scope + "." + path);
        request.setFieldName(key);
        request.setDataType("STRING");
        request.setDefaultValue(defaultValue);
        request.setValueSource("IMPORT");
        return request;
    }

    private MockMultipartFile file(String path) throws Exception {
        ClassPathResource resource = new ClassPathResource(path);
        return new MockMultipartFile("file", path, "application/json", resource.getInputStream());
    }
}
