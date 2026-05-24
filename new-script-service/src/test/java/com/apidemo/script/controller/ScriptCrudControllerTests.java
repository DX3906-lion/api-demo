package com.apidemo.script.controller;

import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.StepDefinitionRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(statements = {
        "DELETE FROM step_definition",
        "DELETE FROM script_version",
        "DELETE FROM script"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ScriptCrudControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createScriptShouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/api/scripts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scriptRequest("登录接口测试"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("000000"))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.currentVersion.versionStatus").value("DRAFT"));
    }

    @Test
    void getScriptDetailShouldReturnSuccess() throws Exception {
        JsonNode created = createScript("查询脚本详情");
        String scriptId = created.path("data").path("id").asText();

        mockMvc.perform(get("/api/scripts/{scriptId}", scriptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(scriptId))
                .andExpect(jsonPath("$.data.currentVersion.id").value(created.path("data").path("currentVersionId").asText()));
    }

    @Test
    void publishVersionShouldReturnSuccess() throws Exception {
        JsonNode created = createScript("发布脚本版本");
        String scriptId = created.path("data").path("id").asText();
        String versionId = created.path("data").path("currentVersionId").asText();

        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/publish", scriptId, versionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.versionStatus").value("PUBLISHED"))
                .andExpect(jsonPath("$.data.publishedAt").exists());
    }

    @Test
    void createStepShouldReturnSuccess() throws Exception {
        JsonNode created = createScript("新增步骤脚本");
        String scriptId = created.path("data").path("id").asText();
        String versionId = created.path("data").path("currentVersionId").asText();

        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/steps", scriptId, versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepRequest("登录请求"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.stepType").value("HTTP"))
                .andExpect(jsonPath("$.data.requestConfig").value("{}"))
                .andExpect(jsonPath("$.data.enabled").value(1));
    }

    @Test
    void publishedVersionCreateStepShouldFail() throws Exception {
        JsonNode created = createScript("发布后步骤失败");
        String scriptId = created.path("data").path("id").asText();
        String versionId = created.path("data").path("currentVersionId").asText();
        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/publish", scriptId, versionId))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/steps", scriptId, versionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stepRequest("发布后新增"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("409002"));
    }

    private JsonNode createScript(String name) throws Exception {
        MvcResult result = mockMvc.perform(post("/api/scripts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scriptRequest(name))))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString());
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
}
