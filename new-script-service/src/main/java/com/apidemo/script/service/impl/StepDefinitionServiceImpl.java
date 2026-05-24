package com.apidemo.script.service.impl;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.common.util.IdGenerator;
import com.apidemo.script.constant.ScriptConstants;
import com.apidemo.script.dto.StepDefinitionRequest;
import com.apidemo.script.dto.StepDefinitionResponse;
import com.apidemo.script.entity.ScriptVersionEntity;
import com.apidemo.script.entity.StepDefinitionEntity;
import com.apidemo.script.mapper.ScriptVersionMapper;
import com.apidemo.script.mapper.StepDefinitionMapper;
import com.apidemo.script.service.StepDefinitionService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default step definition service implementation.
 */
@Service
public class StepDefinitionServiceImpl implements StepDefinitionService {

    private final ScriptVersionMapper scriptVersionMapper;
    private final StepDefinitionMapper stepDefinitionMapper;

    public StepDefinitionServiceImpl(ScriptVersionMapper scriptVersionMapper,
                                     StepDefinitionMapper stepDefinitionMapper) {
        this.scriptVersionMapper = scriptVersionMapper;
        this.stepDefinitionMapper = stepDefinitionMapper;
    }

    /**
     * Creates a step after validating that the version is DRAFT.
     *
     * @param scriptId script id
     * @param versionId version id
     * @param request step creation request
     * @return created step detail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StepDefinitionResponse createStep(String scriptId, String versionId, StepDefinitionRequest request) {
        assertDraftVersion(scriptId, versionId);
        LocalDateTime now = LocalDateTime.now();
        StepDefinitionEntity step = buildStepEntity(scriptId, versionId, request);
        step.setId(IdGenerator.nextId());
        step.setCreatedTime(now);
        step.setUpdatedTime(now);
        step.setDeleted(ScriptConstants.NOT_DELETED);
        stepDefinitionMapper.insert(step);
        return toResponse(step);
    }

    /**
     * Updates a step after validating that the version is DRAFT.
     *
     * @param scriptId script id
     * @param versionId version id
     * @param stepId step id
     * @param request step update request
     * @return updated step detail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StepDefinitionResponse updateStep(String scriptId, String versionId, String stepId, StepDefinitionRequest request) {
        assertDraftVersion(scriptId, versionId);
        StepDefinitionEntity existingStep = getExistingStep(scriptId, versionId, stepId);
        StepDefinitionEntity step = buildStepEntity(scriptId, versionId, request);
        step.setId(existingStep.getId());
        step.setCreatedTime(existingStep.getCreatedTime());
        step.setUpdatedTime(LocalDateTime.now());
        step.setDeleted(ScriptConstants.NOT_DELETED);
        stepDefinitionMapper.update(step);
        return toResponse(getExistingStep(scriptId, versionId, stepId));
    }

    /**
     * Logically deletes a step after validating that the version is DRAFT.
     *
     * @param scriptId script id
     * @param versionId version id
     * @param stepId step id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStep(String scriptId, String versionId, String stepId) {
        assertDraftVersion(scriptId, versionId);
        getExistingStep(scriptId, versionId, stepId);
        stepDefinitionMapper.logicalDelete(stepId, scriptId, versionId, LocalDateTime.now(), null);
    }

    /**
     * Lists active steps after validating that the version belongs to the script.
     *
     * @param scriptId script id
     * @param versionId version id
     * @return active step list
     */
    @Override
    public List<StepDefinitionResponse> listSteps(String scriptId, String versionId) {
        getExistingVersion(scriptId, versionId);
        List<StepDefinitionEntity> steps = stepDefinitionMapper.listByVersion(scriptId, versionId);
        List<StepDefinitionResponse> responses = new ArrayList<StepDefinitionResponse>();
        for (StepDefinitionEntity step : steps) {
            responses.add(toResponse(step));
        }
        return responses;
    }

    private ScriptVersionEntity assertDraftVersion(String scriptId, String versionId) {
        ScriptVersionEntity version = getExistingVersion(scriptId, versionId);
        if (!ScriptConstants.STATUS_DRAFT.equals(version.getVersionStatus())) {
            throw new BizException(ErrorCode.VERSION_STATUS_INVALID, "已发布版本不可修改步骤");
        }
        return version;
    }

    private ScriptVersionEntity getExistingVersion(String scriptId, String versionId) {
        ScriptVersionEntity version = scriptVersionMapper.selectByScriptIdAndId(scriptId, versionId);
        if (version == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "脚本版本不存在");
        }
        return version;
    }

    private StepDefinitionEntity getExistingStep(String scriptId, String versionId, String stepId) {
        StepDefinitionEntity step = stepDefinitionMapper.selectByScriptVersionAndId(scriptId, versionId, stepId);
        if (step == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "步骤不存在");
        }
        return step;
    }

    private StepDefinitionEntity buildStepEntity(String scriptId, String versionId, StepDefinitionRequest request) {
        StepDefinitionEntity step = new StepDefinitionEntity();
        step.setScriptId(scriptId);
        step.setScriptVersionId(versionId);
        step.setParentStepId(request.getParentStepId());
        step.setStepType(request.getStepType());
        step.setName(request.getName());
        step.setSortNo(request.getSortNo());
        step.setRequestMethod(request.getRequestMethod());
        step.setRequestUrl(request.getRequestUrl());
        step.setRequestConfig(defaultJson(request.getRequestConfig()));
        step.setAssertionConfig(defaultJson(request.getAssertionConfig()));
        step.setExtractorConfig(defaultJson(request.getExtractorConfig()));
        step.setEnabled(request.getEnabled() == null ? ScriptConstants.ENABLED : request.getEnabled());
        return step;
    }

    private String defaultJson(String value) {
        return value == null || value.trim().isEmpty() ? ScriptConstants.EMPTY_JSON_OBJECT : value;
    }

    private StepDefinitionResponse toResponse(StepDefinitionEntity step) {
        StepDefinitionResponse response = new StepDefinitionResponse();
        response.setId(step.getId());
        response.setScriptId(step.getScriptId());
        response.setScriptVersionId(step.getScriptVersionId());
        response.setParentStepId(step.getParentStepId());
        response.setStepType(step.getStepType());
        response.setName(step.getName());
        response.setSortNo(step.getSortNo());
        response.setRequestMethod(step.getRequestMethod());
        response.setRequestUrl(step.getRequestUrl());
        response.setRequestConfig(step.getRequestConfig());
        response.setAssertionConfig(step.getAssertionConfig());
        response.setExtractorConfig(step.getExtractorConfig());
        response.setEnabled(step.getEnabled());
        response.setCreatedTime(step.getCreatedTime());
        response.setUpdatedTime(step.getUpdatedTime());
        return response;
    }
}
