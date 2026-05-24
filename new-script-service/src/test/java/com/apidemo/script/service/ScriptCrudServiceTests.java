package com.apidemo.script.service;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.ScriptDetailResponse;
import com.apidemo.script.dto.ScriptVersionCreateRequest;
import com.apidemo.script.dto.ScriptVersionResponse;
import com.apidemo.script.dto.StepDefinitionRequest;
import com.apidemo.script.dto.StepDefinitionResponse;
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
        "DELETE FROM step_definition",
        "DELETE FROM script_version",
        "DELETE FROM script"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ScriptCrudServiceTests {

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private ScriptVersionService scriptVersionService;

    @Autowired
    private StepDefinitionService stepDefinitionService;

    @Test
    void createScriptShouldCreateDraftVersion() {
        ScriptDetailResponse script = scriptService.createScript(scriptRequest("登录接口测试"));

        assertNotNull(script.getId());
        assertEquals("DRAFT", script.getStatus());
        assertNotNull(script.getCurrentVersion());
        assertEquals(script.getId(), script.getCurrentVersion().getScriptId());
        assertEquals(Integer.valueOf(1), script.getCurrentVersion().getVersionNo());
        assertEquals("DRAFT", script.getCurrentVersion().getVersionStatus());
    }

    @Test
    void sameScriptCannotCreateDuplicateDraftVersion() {
        ScriptDetailResponse script = scriptService.createScript(scriptRequest("重复草稿检查"));

        BizException exception = assertThrows(BizException.class, () ->
                scriptVersionService.createDraftVersion(script.getId(), new ScriptVersionCreateRequest()));

        assertEquals(ErrorCode.BUSINESS_CONFLICT.getCode(), exception.getCode());
    }

    @Test
    void onlyDraftVersionCanBePublished() {
        ScriptDetailResponse script = scriptService.createScript(scriptRequest("发布状态检查"));
        ScriptVersionResponse published = scriptVersionService.publishVersion(script.getId(), script.getCurrentVersionId());

        assertEquals("PUBLISHED", published.getVersionStatus());
        assertNotNull(published.getPublishedAt());

        BizException exception = assertThrows(BizException.class, () ->
                scriptVersionService.publishVersion(script.getId(), script.getCurrentVersionId()));

        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), exception.getCode());
    }

    @Test
    void publishedVersionCannotCreateUpdateOrDeleteSteps() {
        ScriptDetailResponse script = scriptService.createScript(scriptRequest("步骤只读检查"));
        StepDefinitionResponse step = stepDefinitionService.createStep(script.getId(), script.getCurrentVersionId(), stepRequest("登录请求"));
        scriptVersionService.publishVersion(script.getId(), script.getCurrentVersionId());

        BizException createException = assertThrows(BizException.class, () ->
                stepDefinitionService.createStep(script.getId(), script.getCurrentVersionId(), stepRequest("发布后新增")));
        BizException updateException = assertThrows(BizException.class, () ->
                stepDefinitionService.updateStep(script.getId(), script.getCurrentVersionId(), step.getId(), stepRequest("发布后更新")));
        BizException deleteException = assertThrows(BizException.class, () ->
                stepDefinitionService.deleteStep(script.getId(), script.getCurrentVersionId(), step.getId()));

        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), createException.getCode());
        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), updateException.getCode());
        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), deleteException.getCode());
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
