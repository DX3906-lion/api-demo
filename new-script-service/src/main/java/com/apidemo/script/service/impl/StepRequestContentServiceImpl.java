package com.apidemo.script.service.impl;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.common.util.IdGenerator;
import com.apidemo.script.constant.ScriptConstants;
import com.apidemo.script.dto.StepPayloadContentBatchSaveRequest;
import com.apidemo.script.dto.StepPayloadContentItemRequest;
import com.apidemo.script.dto.StepPayloadContentResponse;
import com.apidemo.script.dto.StepRequestConfigRequest;
import com.apidemo.script.dto.StepRequestConfigResponse;
import com.apidemo.script.entity.ScriptVersionEntity;
import com.apidemo.script.entity.StepDefinitionEntity;
import com.apidemo.script.entity.StepPayloadContentEntity;
import com.apidemo.script.entity.StepRequestConfigEntity;
import com.apidemo.script.mapper.ScriptVersionMapper;
import com.apidemo.script.mapper.StepDefinitionMapper;
import com.apidemo.script.mapper.StepPayloadContentMapper;
import com.apidemo.script.mapper.StepRequestConfigMapper;
import com.apidemo.script.service.StepRequestContentService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default service implementation for request configuration and payload content.
 */
@Service
public class StepRequestContentServiceImpl implements StepRequestContentService {

    private final ScriptVersionMapper scriptVersionMapper;
    private final StepDefinitionMapper stepDefinitionMapper;
    private final StepRequestConfigMapper stepRequestConfigMapper;
    private final StepPayloadContentMapper stepPayloadContentMapper;

    public StepRequestContentServiceImpl(ScriptVersionMapper scriptVersionMapper,
                                         StepDefinitionMapper stepDefinitionMapper,
                                         StepRequestConfigMapper stepRequestConfigMapper,
                                         StepPayloadContentMapper stepPayloadContentMapper) {
        this.scriptVersionMapper = scriptVersionMapper;
        this.stepDefinitionMapper = stepDefinitionMapper;
        this.stepRequestConfigMapper = stepRequestConfigMapper;
        this.stepPayloadContentMapper = stepPayloadContentMapper;
    }

    /**
     * Gets request configuration by step id, falling back to compatibility fields when needed.
     *
     * @param stepId step id
     * @return request configuration response
     */
    @Override
    public StepRequestConfigResponse getRequestConfig(String stepId) {
        StepDefinitionEntity step = getExistingStep(stepId);
        StepRequestConfigEntity config = stepRequestConfigMapper.selectByStepId(stepId);
        if (config != null) {
            return toRequestConfigResponse(config);
        }
        return compatibilityResponse(step);
    }

    /**
     * Saves request configuration after validating the owning version is DRAFT.
     *
     * @param stepId step id
     * @param request request configuration payload
     * @return saved request configuration response
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public StepRequestConfigResponse saveRequestConfig(String stepId, StepRequestConfigRequest request) {
        StepDefinitionEntity step = assertDraftStep(stepId);
        if (request == null) {
            throw new BizException(ErrorCode.PARAM_INVALID, "请求配置不能为空");
        }
        LocalDateTime now = LocalDateTime.now();
        StepRequestConfigEntity existing = stepRequestConfigMapper.selectByStepId(stepId);
        StepRequestConfigEntity config = buildRequestConfig(stepId, request, now);
        if (existing == null) {
            config.setId(IdGenerator.nextId());
            config.setCreatedTime(now);
            config.setDeleted(ScriptConstants.NOT_DELETED);
            stepRequestConfigMapper.insert(config);
        } else {
            config.setId(existing.getId());
            config.setCreatedTime(existing.getCreatedTime());
            config.setDeleted(ScriptConstants.NOT_DELETED);
            stepRequestConfigMapper.update(config);
        }
        stepDefinitionMapper.updateRequestCompatibility(step.getId(), config.getMethod(), config.getUrlTemplate(),
                config.getConfigJson(), now, null);
        return toRequestConfigResponse(stepRequestConfigMapper.selectByStepId(stepId));
    }

    /**
     * Lists payload content rows after verifying the step exists.
     *
     * @param stepId step id
     * @return payload content list
     */
    @Override
    public List<StepPayloadContentResponse> listPayloadContents(String stepId) {
        getExistingStep(stepId);
        List<StepPayloadContentEntity> contents = stepPayloadContentMapper.listByStepId(stepId);
        List<StepPayloadContentResponse> responses = new ArrayList<StepPayloadContentResponse>();
        for (StepPayloadContentEntity content : contents) {
            responses.add(toPayloadContentResponse(content));
        }
        return responses;
    }

    /**
     * Saves payload content rows after validating the owning version is DRAFT.
     *
     * @param stepId step id
     * @param request payload content batch request
     * @return saved payload content rows
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StepPayloadContentResponse> savePayloadContents(String stepId, StepPayloadContentBatchSaveRequest request) {
        assertDraftStep(stepId);
        if (request == null || request.getContents() == null || request.getContents().isEmpty()) {
            throw new BizException(ErrorCode.PARAM_INVALID, "Payload 内容不能为空");
        }
        LocalDateTime now = LocalDateTime.now();
        List<StepPayloadContentResponse> responses = new ArrayList<StepPayloadContentResponse>();
        for (StepPayloadContentItemRequest item : request.getContents()) {
            if (item == null || isBlank(item.getDirection()) || isBlank(item.getLocation())) {
                throw new BizException(ErrorCode.PARAM_INVALID, "Payload 方向和位置不能为空");
            }
            String direction = normalize(item.getDirection());
            String location = normalize(item.getLocation());
            StepPayloadContentEntity existing = stepPayloadContentMapper.selectByStepDirectionLocation(stepId, direction, location);
            StepPayloadContentEntity content = buildPayloadContent(stepId, item, direction, location, now);
            if (existing == null) {
                content.setId(IdGenerator.nextId());
                content.setCreatedTime(now);
                content.setDeleted(ScriptConstants.NOT_DELETED);
                stepPayloadContentMapper.insert(content);
            } else {
                content.setId(existing.getId());
                content.setCreatedTime(existing.getCreatedTime());
                content.setDeleted(ScriptConstants.NOT_DELETED);
                stepPayloadContentMapper.update(content);
            }
            responses.add(toPayloadContentResponse(
                    stepPayloadContentMapper.selectByStepDirectionLocation(stepId, direction, location)));
        }
        return responses;
    }

    private StepDefinitionEntity assertDraftStep(String stepId) {
        StepDefinitionEntity step = getExistingStep(stepId);
        ScriptVersionEntity version = scriptVersionMapper.selectByScriptIdAndId(step.getScriptId(), step.getScriptVersionId());
        if (version == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "脚本版本不存在");
        }
        if (!ScriptConstants.STATUS_DRAFT.equals(version.getVersionStatus())) {
            throw new BizException(ErrorCode.VERSION_STATUS_INVALID, "已发布版本不可修改请求配置");
        }
        return step;
    }

    private StepDefinitionEntity getExistingStep(String stepId) {
        StepDefinitionEntity step = stepDefinitionMapper.selectById(stepId);
        if (step == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "步骤不存在");
        }
        return step;
    }

    private StepRequestConfigEntity buildRequestConfig(String stepId, StepRequestConfigRequest request, LocalDateTime now) {
        StepRequestConfigEntity config = new StepRequestConfigEntity();
        config.setStepId(stepId);
        config.setMethod(normalize(request.getMethod()));
        config.setUrlTemplate(request.getUrlTemplate());
        config.setProtocolType(defaultString(normalize(request.getProtocolType()), ScriptConstants.PROTOCOL_HTTP));
        config.setContentType(request.getContentType());
        config.setBodyFormat(normalize(request.getBodyFormat()));
        config.setCharset(defaultString(request.getCharset(), "UTF-8"));
        config.setTimeoutMs(request.getTimeoutMs());
        config.setFollowRedirect(defaultString(request.getFollowRedirect(), ScriptConstants.FOLLOW_REDIRECT_TRUE));
        config.setConfigJson(defaultString(request.getConfigJson(), ScriptConstants.EMPTY_JSON_OBJECT));
        config.setUpdatedTime(now);
        return config;
    }

    private StepPayloadContentEntity buildPayloadContent(String stepId,
                                                         StepPayloadContentItemRequest item,
                                                         String direction,
                                                         String location,
                                                         LocalDateTime now) {
        StepPayloadContentEntity content = new StepPayloadContentEntity();
        content.setStepId(stepId);
        content.setDirection(direction);
        content.setLocation(location);
        content.setContentFormat(normalize(item.getContentFormat()));
        content.setRawContent(item.getRawContent());
        content.setParsedContentJson(item.getParsedContentJson());
        content.setContentHash(sha256(defaultString(item.getRawContent(), item.getParsedContentJson())));
        content.setUpdatedTime(now);
        return content;
    }

    private StepRequestConfigResponse compatibilityResponse(StepDefinitionEntity step) {
        StepRequestConfigResponse response = new StepRequestConfigResponse();
        response.setStepId(step.getId());
        response.setMethod(step.getRequestMethod());
        response.setUrlTemplate(step.getRequestUrl());
        response.setProtocolType(ScriptConstants.PROTOCOL_HTTP);
        response.setFollowRedirect(ScriptConstants.FOLLOW_REDIRECT_TRUE);
        response.setConfigJson(defaultString(step.getRequestConfig(), ScriptConstants.EMPTY_JSON_OBJECT));
        response.setCreatedTime(step.getCreatedTime());
        response.setUpdatedTime(step.getUpdatedTime());
        return response;
    }

    private StepRequestConfigResponse toRequestConfigResponse(StepRequestConfigEntity config) {
        StepRequestConfigResponse response = new StepRequestConfigResponse();
        response.setId(config.getId());
        response.setStepId(config.getStepId());
        response.setMethod(config.getMethod());
        response.setUrlTemplate(config.getUrlTemplate());
        response.setProtocolType(config.getProtocolType());
        response.setContentType(config.getContentType());
        response.setBodyFormat(config.getBodyFormat());
        response.setCharset(config.getCharset());
        response.setTimeoutMs(config.getTimeoutMs());
        response.setFollowRedirect(config.getFollowRedirect());
        response.setConfigJson(config.getConfigJson());
        response.setCreatedTime(config.getCreatedTime());
        response.setUpdatedTime(config.getUpdatedTime());
        return response;
    }

    private StepPayloadContentResponse toPayloadContentResponse(StepPayloadContentEntity content) {
        StepPayloadContentResponse response = new StepPayloadContentResponse();
        response.setId(content.getId());
        response.setStepId(content.getStepId());
        response.setDirection(content.getDirection());
        response.setLocation(content.getLocation());
        response.setContentFormat(content.getContentFormat());
        response.setRawContent(content.getRawContent());
        response.setParsedContentJson(content.getParsedContentJson());
        response.setContentHash(content.getContentHash());
        response.setCreatedTime(content.getCreatedTime());
        response.setUpdatedTime(content.getUpdatedTime());
        return response;
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(defaultString(value, "").getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new BizException(ErrorCode.SYSTEM_ERROR, "内容 Hash 计算失败");
        }
    }

    private String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase(Locale.ROOT);
    }

    private String defaultString(String value, String defaultValue) {
        return isBlank(value) ? defaultValue : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
