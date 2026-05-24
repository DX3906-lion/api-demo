package com.apidemo.script.service;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.script.dto.ImportConfirmFieldRequest;
import com.apidemo.script.dto.ImportConfirmRequest;
import com.apidemo.script.dto.ImportConfirmResponse;
import com.apidemo.script.dto.ImportConfirmStepRequest;
import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.ScriptDetailResponse;
import com.apidemo.script.dto.ScriptFieldDefaultResponse;
import com.apidemo.script.dto.StepDefinitionResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {
        "DELETE FROM case_field_value",
        "DELETE FROM case_data_set",
        "DELETE FROM script_field_default",
        "DELETE FROM field_config",
        "DELETE FROM step_definition",
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

    @Test
    void draftVersionCanConfirmImportAndCreateStepFieldAndDefault() {
        ScriptDetailResponse script = scriptService.createScript(scriptRequest("import draft"));

        ImportConfirmResponse response = importService.confirm(script.getId(), script.getCurrentVersionId(), confirmRequest());
        List<StepDefinitionResponse> steps = stepDefinitionService.listSteps(script.getId(), script.getCurrentVersionId());
        List<ScriptFieldDefaultResponse> defaults = fieldConfigService.listDefaults(script.getId(), script.getCurrentVersionId(), null, null);

        assertEquals(Integer.valueOf(1), response.getImportedStepCount());
        assertEquals(Integer.valueOf(2), response.getImportedFieldCount());
        assertEquals(Integer.valueOf(1), response.getImportedDefaultCount());
        assertEquals(1, steps.size());
        assertEquals("POST", steps.get(0).getRequestMethod());
        assertEquals(2, fieldConfigService.listFields(script.getId(), script.getCurrentVersionId(), null, null).size());
        assertFalse(defaults.isEmpty());
        assertEquals("demo-user", defaults.get(0).getDefaultValue());
    }

    @Test
    void publishedVersionCannotConfirmImport() {
        ScriptDetailResponse script = scriptService.createScript(scriptRequest("import published"));
        scriptVersionService.publishVersion(script.getId(), script.getCurrentVersionId());

        BizException exception = assertThrows(BizException.class, () ->
                importService.confirm(script.getId(), script.getCurrentVersionId(), confirmRequest()));

        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), exception.getCode());
    }

    private ScriptCreateRequest scriptRequest(String name) {
        ScriptCreateRequest request = new ScriptCreateRequest();
        request.setName(name);
        request.setDescription(name + " description");
        return request;
    }

    private ImportConfirmRequest confirmRequest() {
        ImportConfirmStepRequest step = new ImportConfirmStepRequest();
        step.setName("Login Request");
        step.setStepType("HTTP");
        step.setRequestMethod("POST");
        step.setRequestUrl("http://localhost:8080/login");
        step.setRequestConfig("{\"headers\":[],\"queryString\":[],\"postData\":{}}");
        step.setFields(Arrays.asList(field("REQUEST_BODY", "$.username", "username", "demo-user"),
                field("REQUEST_HEADER", "header.Content-Type", "Content-Type", null)));

        ImportConfirmRequest request = new ImportConfirmRequest();
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
}
