package com.apidemo.script.controller;

import com.apidemo.script.dto.ImportConfirmFieldRequest;
import com.apidemo.script.dto.ImportConfirmRequest;
import com.apidemo.script.dto.ImportConfirmStepRequest;
import com.apidemo.script.dto.ScriptCreateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
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
class ImportControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void previewHarUploadShouldReturnSuccess() throws Exception {
        mockMvc.perform(multipart("/api/imports/preview")
                        .file(file("sample.har"))
                        .param("importType", "HAR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.importType").value("HAR"))
                .andExpect(jsonPath("$.data.steps[0].requestMethod").value("POST"));
    }

    @Test
    void previewPostmanUploadShouldReturnSuccess() throws Exception {
        mockMvc.perform(multipart("/api/imports/preview")
                        .file(file("sample-postman-collection.json"))
                        .param("importType", "POSTMAN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.importType").value("POSTMAN"))
                .andExpect(jsonPath("$.data.steps[0].name").value("Login Request"));
    }

    @Test
    void confirmImportShouldReturnSuccess() throws Exception {
        ScriptContext context = createScript("confirm import");

        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/imports/confirm", context.scriptId, context.versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.importedStepCount").value(1))
                .andExpect(jsonPath("$.data.importedFieldCount").value(1))
                .andExpect(jsonPath("$.data.importedDefaultCount").value(1));
    }

    @Test
    void confirmImportOnPublishedVersionShouldFail() throws Exception {
        ScriptContext context = createScript("published import");
        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/publish", context.scriptId, context.versionId))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/imports/confirm", context.scriptId, context.versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmRequest())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("409002"));
    }

    private ScriptContext createScript(String name) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/scripts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scriptRequest(name))))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return new ScriptContext(root.path("data").path("id").asText(), root.path("data").path("currentVersionId").asText());
    }

    private ScriptCreateRequest scriptRequest(String name) {
        ScriptCreateRequest request = new ScriptCreateRequest();
        request.setName(name);
        request.setDescription(name + " description");
        return request;
    }

    private ImportConfirmRequest confirmRequest() {
        ImportConfirmFieldRequest field = new ImportConfirmFieldRequest();
        field.setFieldScope("REQUEST_BODY");
        field.setFieldPath("$.username");
        field.setFieldKey("username");
        field.setStableFieldKey("Login_Request.REQUEST_BODY.username");
        field.setFieldName("username");
        field.setDataType("STRING");
        field.setDefaultValue("demo-user");
        field.setValueSource("IMPORT");

        ImportConfirmStepRequest step = new ImportConfirmStepRequest();
        step.setName("Login Request");
        step.setStepType("HTTP");
        step.setRequestMethod("POST");
        step.setRequestUrl("http://localhost:8080/login");
        step.setRequestConfig("{\"headers\":[]}");
        step.setFields(Arrays.asList(field));

        ImportConfirmRequest request = new ImportConfirmRequest();
        request.setImportType("HAR");
        request.setSteps(Arrays.asList(step));
        return request;
    }

    private MockMultipartFile file(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return new MockMultipartFile("file", path, "application/json", resource.getInputStream());
    }

    private static class ScriptContext {
        private final String scriptId;
        private final String versionId;

        private ScriptContext(String scriptId, String versionId) {
            this.scriptId = scriptId;
            this.versionId = versionId;
        }
    }
}
