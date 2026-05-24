package com.apidemo.script.service;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.script.dto.CaseDataSetRequest;
import com.apidemo.script.dto.CaseDataSetResponse;
import com.apidemo.script.dto.CaseFieldValueBatchSaveRequest;
import com.apidemo.script.dto.CaseFieldValueResponse;
import com.apidemo.script.dto.CaseFieldValueSaveItem;
import com.apidemo.script.dto.FieldConfigRequest;
import com.apidemo.script.dto.FieldConfigResponse;
import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.ScriptDetailResponse;
import com.apidemo.script.dto.ScriptFieldDefaultRequest;
import com.apidemo.script.dto.ScriptFieldDefaultResponse;
import com.apidemo.script.dto.StepDefinitionRequest;
import com.apidemo.script.dto.StepDefinitionResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
class FieldCaseDataServiceTests {

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private ScriptVersionService scriptVersionService;

    @Autowired
    private StepDefinitionService stepDefinitionService;

    @Autowired
    private FieldConfigService fieldConfigService;

    @Autowired
    private CaseDataSetService caseDataSetService;

    @Test
    void draftVersionCanCreateFieldConfig() {
        ScriptContext context = createScriptWithStep("字段配置草稿");

        FieldConfigResponse field = fieldConfigService.createField(context.scriptId, context.versionId, fieldRequest(context.stepId, "$.username", "login.username"));

        assertNotNull(field.getId());
        assertEquals(context.scriptId, field.getScriptId());
        assertEquals(context.versionId, field.getScriptVersionId());
        assertEquals(Integer.valueOf(0), field.getRequired());
    }

    @Test
    void publishedVersionCannotCreateUpdateOrDeleteFieldConfig() {
        ScriptContext context = createScriptWithStep("字段配置发布只读");
        FieldConfigResponse field = fieldConfigService.createField(context.scriptId, context.versionId, fieldRequest(context.stepId, "$.username", "login.username"));
        scriptVersionService.publishVersion(context.scriptId, context.versionId);

        BizException createException = assertThrows(BizException.class, () ->
                fieldConfigService.createField(context.scriptId, context.versionId, fieldRequest(context.stepId, "$.password", "login.password")));
        BizException updateException = assertThrows(BizException.class, () ->
                fieldConfigService.updateField(context.scriptId, context.versionId, field.getId(), fieldRequest(context.stepId, "$.username2", "login.username2")));
        BizException deleteException = assertThrows(BizException.class, () ->
                fieldConfigService.deleteField(context.scriptId, context.versionId, field.getId()));

        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), createException.getCode());
        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), updateException.getCode());
        assertEquals(ErrorCode.VERSION_STATUS_INVALID.getCode(), deleteException.getCode());
    }

    @Test
    void saveDefaultTwiceShouldUpdateExistingRow() {
        ScriptContext context = createScriptWithStep("字段默认值更新");
        FieldConfigResponse field = fieldConfigService.createField(context.scriptId, context.versionId, fieldRequest(context.stepId, "$.username", "login.username"));

        fieldConfigService.saveDefault(context.scriptId, context.versionId, field.getId(), defaultRequest("demo-user"));
        ScriptFieldDefaultResponse updated = fieldConfigService.saveDefault(context.scriptId, context.versionId, field.getId(), defaultRequest("demo-user-2"));
        List<ScriptFieldDefaultResponse> defaults = fieldConfigService.listDefaults(context.scriptId, context.versionId, null, null);

        assertEquals(1, defaults.size());
        assertEquals(updated.getId(), defaults.get(0).getId());
        assertEquals("demo-user-2", defaults.get(0).getDefaultValue());
    }

    @Test
    void createCaseDataSetShouldSucceed() {
        ScriptContext context = createScriptWithStep("用例数据集创建");

        CaseDataSetResponse caseDataSet = caseDataSetService.createCaseDataSet(context.scriptId, context.versionId, caseDataSetRequest("正常登录用例"));

        assertNotNull(caseDataSet.getId());
        assertEquals("ENABLED", caseDataSet.getStatus());
    }

    @Test
    void batchSaveCaseFieldValuesShouldSucceedOnPublishedVersion() {
        ScriptContext context = createScriptWithStep("用例覆盖值保存");
        FieldConfigResponse field = fieldConfigService.createField(context.scriptId, context.versionId, fieldRequest(context.stepId, "$.username", "login.username"));
        scriptVersionService.publishVersion(context.scriptId, context.versionId);
        CaseDataSetResponse caseDataSet = caseDataSetService.createCaseDataSet(context.scriptId, context.versionId, caseDataSetRequest("正常登录用例"));

        List<CaseFieldValueResponse> values = caseDataSetService.saveCaseFieldValues(context.scriptId, context.versionId, caseDataSet.getId(), batchRequest(field.getId(), "demo-user"));

        assertEquals(1, values.size());
        assertEquals(field.getId(), values.get(0).getFieldConfigId());
        assertEquals("demo-user", values.get(0).getValue());
    }

    @Test
    void caseFieldValueCannotReferenceOtherVersionFieldConfig() {
        ScriptContext caseContext = createScriptWithStep("用例版本");
        ScriptContext otherContext = createScriptWithStep("其他版本字段");
        FieldConfigResponse otherField = fieldConfigService.createField(otherContext.scriptId, otherContext.versionId, fieldRequest(otherContext.stepId, "$.other", "other.field"));
        CaseDataSetResponse caseDataSet = caseDataSetService.createCaseDataSet(caseContext.scriptId, caseContext.versionId, caseDataSetRequest("正常登录用例"));

        BizException exception = assertThrows(BizException.class, () ->
                caseDataSetService.saveCaseFieldValues(caseContext.scriptId, caseContext.versionId, caseDataSet.getId(), batchRequest(otherField.getId(), "wrong")));

        assertEquals(ErrorCode.RESOURCE_NOT_FOUND.getCode(), exception.getCode());
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
