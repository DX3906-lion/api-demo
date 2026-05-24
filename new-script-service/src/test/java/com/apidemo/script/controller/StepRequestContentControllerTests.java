package com.apidemo.script.controller;

import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.StepDefinitionRequest;
import com.apidemo.script.dto.StepPayloadContentBatchSaveRequest;
import com.apidemo.script.dto.StepPayloadContentItemRequest;
import com.apidemo.script.dto.StepRequestConfigRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
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
        "DELETE FROM step_payload_content",
        "DELETE FROM step_request_config",
        "DELETE FROM case_field_value",
        "DELETE FROM case_data_set",
        "DELETE FROM script_field_default",
        "DELETE FROM field_config",
        "DELETE FROM step_definition",
        "DELETE FROM script_version",
        "DELETE FROM script"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StepRequestContentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void requestConfigEndpointsShouldSaveAndQueryByStepId() throws Exception {
        ScriptContext context = createScriptWithStep("请求配置接口");

        mockMvc.perform(put("/api/steps/{stepId}/request-config", context.stepId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestConfig("POST"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.method").value("POST"))
                .andExpect(jsonPath("$.data.bodyFormat").value("JSON"));

        mockMvc.perform(get("/api/steps/{stepId}/request-config", context.stepId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.stepId").value(context.stepId))
                .andExpect(jsonPath("$.data.contentType").value("application/json"));
    }

    @Test
    void payloadEndpointsShouldSaveAndListByStepId() throws Exception {
        ScriptContext context = createScriptWithStep("Payload 接口");

        mockMvc.perform(put("/api/steps/{stepId}/payload", context.stepId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payloadBatch())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].direction").value("REQUEST"))
                .andExpect(jsonPath("$.data[0].location").value("BODY"));

        mockMvc.perform(get("/api/steps/{stepId}/payload", context.stepId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].contentHash").isNotEmpty());
    }

    @Test
    void publishedVersionRequestContentSaveShouldFail() throws Exception {
        ScriptContext context = createScriptWithStep("发布后请求内容接口");
        mockMvc.perform(post("/api/scripts/{scriptId}/versions/{versionId}/publish", context.scriptId, context.versionId))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/steps/{stepId}/request-config", context.stepId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestConfig("POST"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("409002"));
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

    private StepRequestConfigRequest requestConfig(String method) {
        StepRequestConfigRequest request = new StepRequestConfigRequest();
        request.setMethod(method);
        request.setUrlTemplate("http://localhost:8080/login");
        request.setProtocolType("HTTP");
        request.setContentType("application/json");
        request.setBodyFormat("JSON");
        request.setCharset("UTF-8");
        request.setTimeoutMs(30000);
        request.setFollowRedirect("1");
        request.setConfigJson("{\"headers\":[]}");
        return request;
    }

    private StepPayloadContentBatchSaveRequest payloadBatch() {
        StepPayloadContentBatchSaveRequest request = new StepPayloadContentBatchSaveRequest();
        request.setContents(Arrays.asList(
                payload("REQUEST", "BODY", "JSON", "{\"username\":\"demo-user\"}", "{\"username\":\"demo-user\"}"),
                payload("REQUEST", "HEADER", "KEY_VALUE", "Content-Type: application/json",
                        "[{\"name\":\"Content-Type\",\"value\":\"application/json\"}]")
        ));
        return request;
    }

    private StepPayloadContentItemRequest payload(String direction,
                                                  String location,
                                                  String contentFormat,
                                                  String rawContent,
                                                  String parsedContentJson) {
        StepPayloadContentItemRequest item = new StepPayloadContentItemRequest();
        item.setDirection(direction);
        item.setLocation(location);
        item.setContentFormat(contentFormat);
        item.setRawContent(rawContent);
        item.setParsedContentJson(parsedContentJson);
        return item;
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
