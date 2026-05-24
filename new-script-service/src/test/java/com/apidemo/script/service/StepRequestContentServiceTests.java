package com.apidemo.script.service;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.ScriptDetailResponse;
import com.apidemo.script.dto.StepDefinitionRequest;
import com.apidemo.script.dto.StepDefinitionResponse;
import com.apidemo.script.dto.StepPayloadContentBatchSaveRequest;
import com.apidemo.script.dto.StepPayloadContentItemRequest;
import com.apidemo.script.dto.StepPayloadContentResponse;
import com.apidemo.script.dto.StepRequestConfigRequest;
import com.apidemo.script.dto.StepRequestConfigResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        "DELETE FROM script_version",
        "DELETE FROM script"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class StepRequestContentServiceTests {

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private ScriptVersionService scriptVersionService;

    @Autowired
    private StepDefinitionService stepDefinitionService;

    @Autowired
    private StepRequestContentService stepRequestContentService;

    @Test
    void draftVersionCanSaveAndQueryRequestConfig() {
        ScriptContext context = createScriptWithStep("请求配置保存");

        StepRequestConfigResponse saved = stepRequestContentService.saveRequestConfig(context.stepId, requestConfig("POST"));
        StepRequestConfigResponse loaded = stepRequestContentService.getRequestConfig(context.stepId);

        assertNotNull(saved.getId());
        assertEquals(saved.getId(), loaded.getId());
        assertEquals("POST", loaded.getMethod());
        assertEquals("http://localhost:8080/login", loaded.getUrlTemplate());
        assertEquals("HTTP", loaded.getProtocolType());
        assertEquals("application/json", loaded.getContentType());
        assertEquals("1", loaded.getFollowRedirect());
    }

    @Test
    void savingRequestConfigTwiceUpdatesExistingRow() {
        ScriptContext context = createScriptWithStep("请求配置更新");

        StepRequestConfigResponse first = stepRequestContentService.saveRequestConfig(context.stepId, requestConfig("POST"));
        StepRequestConfigRequest update = requestConfig("PUT");
        update.setUrlTemplate("http://localhost:8080/users/1");
        StepRequestConfigResponse second = stepRequestContentService.saveRequestConfig(context.stepId, update);

        assertEquals(first.getId(), second.getId());
        assertEquals("PUT", second.getMethod());
        assertEquals("http://localhost:8080/users/1", second.getUrlTemplate());
    }

    @Test
    void draftVersionCanSaveAndListPayloadContents() {
        ScriptContext context = createScriptWithStep("Payload 保存");

        List<StepPayloadContentResponse> saved = stepRequestContentService.savePayloadContents(context.stepId, payloadBatch());
        List<StepPayloadContentResponse> loaded = stepRequestContentService.listPayloadContents(context.stepId);

        assertEquals(2, saved.size());
        assertEquals(2, loaded.size());
        assertEquals("REQUEST", loaded.get(0).getDirection());
        assertEquals("BODY", loaded.get(0).getLocation());
        assertEquals("JSON", loaded.get(0).getContentFormat());
        assertEquals("{\"username\":\"demo-user\"}", loaded.get(0).getRawContent());
        assertNotNull(loaded.get(0).getContentHash());
    }

    @Test
    void savingPayloadTwiceUpdatesExistingDirectionAndLocation() {
        ScriptContext context = createScriptWithStep("Payload 更新");
        stepRequestContentService.savePayloadContents(context.stepId, payloadBatch());

        StepPayloadContentBatchSaveRequest update = new StepPayloadContentBatchSaveRequest();
        update.setContents(Arrays.asList(payload("REQUEST", "BODY", "JSON",
                "{\"username\":\"demo-user-2\"}", "{\"username\":\"demo-user-2\"}")));
        List<StepPayloadContentResponse> saved = stepRequestContentService.savePayloadContents(context.stepId, update);
        List<StepPayloadContentResponse> loaded = stepRequestContentService.listPayloadContents(context.stepId);

        assertEquals(1, saved.size());
        assertEquals(2, loaded.size());
        assertEquals("{\"username\":\"demo-user-2\"}", loaded.get(0).getRawContent());
    }

    @Test
    void publishedVersionCannotSaveRequestConfigOrPayload() {
        ScriptContext context = createScriptWithStep("发布后请求内容只读");
        scriptVersionService.publishVersion(context.scriptId, context.versionId);

        BizException configException = assertThrows(BizException.class, () ->
                stepRequestContentService.saveRequestConfig(context.stepId, requestConfig("POST")));
        BizException payloadException = assertThrows(BizException.class, () ->
                stepRequestContentService.savePayloadContents(context.stepId, payloadBatch()));

        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), configException.getCode());
        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), payloadException.getCode());
    }

    private ScriptContext createScriptWithStep(String name) {
        ScriptCreateRequest scriptRequest = new ScriptCreateRequest();
        scriptRequest.setName(name);
        scriptRequest.setDescription(name + "描述");
        ScriptDetailResponse script = scriptService.createScript(scriptRequest);
        StepDefinitionResponse step = stepDefinitionService.createStep(script.getId(), script.getCurrentVersionId(), stepRequest("登录请求"));
        return new ScriptContext(script.getId(), script.getCurrentVersionId(), step.getId());
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
