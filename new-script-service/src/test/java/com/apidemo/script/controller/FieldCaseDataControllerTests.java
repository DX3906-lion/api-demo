package com.apidemo.script.controller;

import com.apidemo.script.dto.CaseDataSetRequest;
import com.apidemo.script.dto.CaseFieldValueBatchSaveRequest;
import com.apidemo.script.dto.CaseFieldValueSaveItem;
import com.apidemo.script.dto.FieldConfigRequest;
import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.ScriptFieldDefaultRequest;
import com.apidemo.script.dto.StepDefinitionRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
class FieldCaseDataControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createFieldShouldReturnSuccess() throws Exception {
        ScriptContext context = createScriptWithStep("字段接口创建");

        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/fields", context.scriptId, context.versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fieldRequest(context.stepId, "$.username", "login.username"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.fieldScope").value("REQUEST_BODY"))
                .andExpect(jsonPath("$.data.required").value(0));
    }

    @Test
    void createFieldAfterPublishShouldFail() throws Exception {
        ScriptContext context = createScriptWithStep("发布后字段失败");
        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/publish", context.scriptId, context.versionId))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/fields", context.scriptId, context.versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fieldRequest(context.stepId, "$.username", "login.username"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("409002"));
    }

    @Test
    void saveFieldDefaultShouldReturnSuccess() throws Exception {
        ScriptContext context = createScriptWithStep("字段默认值接口");
        String fieldId = createField(context, "$.username", "login.username");

        mockMvc.perform(put("/api/scripts/{scriptId}/versions/{versionId}/fields/{fieldId}/default", context.scriptId, context.versionId, fieldId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(defaultRequest("demo-user"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.fieldConfigId").value(fieldId))
                .andExpect(jsonPath("$.data.defaultValue").value("demo-user"));
    }

    @Test
    void createCaseDataSetShouldReturnSuccess() throws Exception {
        ScriptContext context = createScriptWithStep("用例数据集接口");

        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/case-data-sets", context.scriptId, context.versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caseDataSetRequest("正常登录用例"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("ENABLED"));
    }

    @Test
    void batchSaveCaseFieldValuesShouldReturnSuccess() throws Exception {
        ScriptContext context = createScriptWithStep("用例覆盖值接口");
        String fieldId = createField(context, "$.username", "login.username");
        String caseDataSetId = createCaseDataSet(context, "正常登录用例");

        mockMvc.perform(put("/api/scripts/{scriptId}/versions/{versionId}/case-data-sets/{caseDataSetId}/values",
                        context.scriptId, context.versionId, caseDataSetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batchRequest(fieldId, "demo-user"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].fieldConfigId").value(fieldId))
                .andExpect(jsonPath("$.data[0].value").value("demo-user"));
    }

    @Test
    void getCaseDataSetDetailShouldReturnSuccess() throws Exception {
        ScriptContext context = createScriptWithStep("用例详情接口");
        String fieldId = createField(context, "$.username", "login.username");
        String caseDataSetId = createCaseDataSet(context, "正常登录用例");
        mockMvc.perform(put("/api/scripts/{scriptId}/versions/{versionId}/case-data-sets/{caseDataSetId}/values",
                        context.scriptId, context.versionId, caseDataSetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batchRequest(fieldId, "demo-user"))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/scripts/{scriptId}/versions/{versionId}/case-data-sets/{caseDataSetId}",
                        context.scriptId, context.versionId, caseDataSetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.caseDataSet.id").value(caseDataSetId))
                .andExpect(jsonPath("$.data.values[0].fieldConfigId").value(fieldId));
    }

    private ScriptContext createScriptWithStep(String name) throws Exception {
        MvcResult scriptResult = mockMvc.perform(post("/api/scripts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scriptRequest(name))))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode scriptNode = objectMapper.readTree(scriptResult.getResponse().getContentAsString());
        String scriptId = scriptNode.path("data").path("id").asText();
        String versionId = scriptNode.path("data").path("currentVersionId").asText();

        MvcResult stepResult = mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/steps", scriptId, versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepRequest("登录请求"))))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode stepNode = objectMapper.readTree(stepResult.getResponse().getContentAsString());
        return new ScriptContext(scriptId, versionId, stepNode.path("data").path("id").asText());
    }

    private String createField(ScriptContext context, String fieldPath, String stableFieldKey) throws Exception {
        MvcResult fieldResult = mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/fields", context.scriptId, context.versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fieldRequest(context.stepId, fieldPath, stableFieldKey))))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode fieldNode = objectMapper.readTree(fieldResult.getResponse().getContentAsString());
        return fieldNode.path("data").path("id").asText();
    }

    private String createCaseDataSet(ScriptContext context, String name) throws Exception {
        MvcResult caseResult = mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/case-data-sets", context.scriptId, context.versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caseDataSetRequest(name))))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode caseNode = objectMapper.readTree(caseResult.getResponse().getContentAsString());
        return caseNode.path("data").path("id").asText();
    }

    private ScriptCreateRequest scriptRequest(String name) {
        ScriptCreateRequest request = new ScriptCreateRequest();
        request.setName(name);
        request.setDescription(name + "描述");
        return request;
    }

    private StepDefinitionRequest stepRequest(String name) {
        StepDefinitionRequest request = new StepDefinitionRequest();
        request.setStepType("HTTP");
        request.setName(name);
        request.setSortNo(1);
        request.setRequestMethod("POST");
        request.setRequestUrl("http://localhost:8080/login");
        return request;
    }

    private FieldConfigRequest fieldRequest(String stepId, String fieldPath, String stableFieldKey) {
        FieldConfigRequest request = new FieldConfigRequest();
        request.setStepId(stepId);
        request.setFieldScope("REQUEST_BODY");
        request.setFieldPath(fieldPath);
        request.setFieldKey(fieldPath.substring(fieldPath.lastIndexOf('.') + 1));
        request.setStableFieldKey(stableFieldKey);
        request.setFieldName("用户名");
        request.setDataType("STRING");
        return request;
    }

    private ScriptFieldDefaultRequest defaultRequest(String value) {
        ScriptFieldDefaultRequest request = new ScriptFieldDefaultRequest();
        request.setDefaultValue(value);
        request.setValueSource("MANUAL");
        return request;
    }

    private CaseDataSetRequest caseDataSetRequest(String name) {
        CaseDataSetRequest request = new CaseDataSetRequest();
        request.setName(name);
        request.setDescription(name + "描述");
        return request;
    }

    private CaseFieldValueBatchSaveRequest batchRequest(String fieldConfigId, String value) {
        CaseFieldValueSaveItem item = new CaseFieldValueSaveItem();
        item.setFieldConfigId(fieldConfigId);
        item.setValue(value);
        item.setValueSource("MANUAL");
        CaseFieldValueBatchSaveRequest request = new CaseFieldValueBatchSaveRequest();
        request.setValues(Collections.singletonList(item));
        return request;
    }

    private static class ScriptContext {
        private final String scriptId;
        private final String versionId;
        private final String stepId;

        private ScriptContext(String scriptId, String versionId, String stepId) {
            this.scriptId = scriptId;
            this.versionId = versionId;
            this.stepId = stepId;
        }
    }
}
