package com.apidemo.script.service.impl;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.common.util.IdGenerator;
import com.apidemo.script.constant.ScriptConstants;
import com.apidemo.script.dto.ScriptVersionCreateRequest;
import com.apidemo.script.dto.ScriptVersionResponse;
import com.apidemo.script.entity.ScriptEntity;
import com.apidemo.script.entity.ScriptVersionEntity;
import com.apidemo.script.mapper.ScriptMapper;
import com.apidemo.script.mapper.ScriptVersionMapper;
import com.apidemo.script.service.ScriptVersionService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default script version service implementation.
 */
@Service
public class ScriptVersionServiceImpl implements ScriptVersionService {

    private final ScriptMapper scriptMapper;
    private final ScriptVersionMapper scriptVersionMapper;

    public ScriptVersionServiceImpl(ScriptMapper scriptMapper, ScriptVersionMapper scriptVersionMapper) {
        this.scriptMapper = scriptMapper;
        this.scriptVersionMapper = scriptVersionMapper;
    }

    /**
     * Creates a new DRAFT version when no other active DRAFT exists.
     *
     * @param scriptId script id
     * @param request draft version creation request
     * @return created version detail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScriptVersionResponse createDraftVersion(String scriptId, ScriptVersionCreateRequest request) {
        getExistingScript(scriptId);
        if (scriptVersionMapper.countDraftByScriptId(scriptId) > 0) {
            throw new BizException(ErrorCode.BUSINESS_CONFLICT, "脚本已存在草稿版本");
        }

        Integer maxVersionNo = scriptVersionMapper.selectMaxVersionNo(scriptId);
        LocalDateTime now = LocalDateTime.now();
        ScriptVersionEntity version = new ScriptVersionEntity();
        version.setId(IdGenerator.nextId());
        version.setScriptId(scriptId);
        version.setVersionNo(maxVersionNo == null ? 1 : maxVersionNo + 1);
        version.setVersionStatus(ScriptConstants.STATUS_DRAFT);
        version.setDescription(request == null ? null : request.getDescription());
        version.setCreatedTime(now);
        version.setUpdatedTime(now);
        version.setDeleted(ScriptConstants.NOT_DELETED);
        scriptVersionMapper.insert(version);
        scriptMapper.updateCurrentVersionAndStatus(scriptId, ScriptConstants.STATUS_DRAFT, version.getId(), now, null);
        return toResponse(version);
    }

    /**
     * Gets a version detail after validating script ownership.
     *
     * @param scriptId script id
     * @param versionId version id
     * @return version detail
     */
    @Override
    public ScriptVersionResponse getVersionDetail(String scriptId, String versionId) {
        return toResponse(getExistingVersion(scriptId, versionId));
    }

    /**
     * Publishes a DRAFT version and updates the script status atomically.
     *
     * @param scriptId script id
     * @param versionId version id
     * @return published version detail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScriptVersionResponse publishVersion(String scriptId, String versionId) {
        getExistingScript(scriptId);
        ScriptVersionEntity version = getExistingVersion(scriptId, versionId);
        if (!ScriptConstants.STATUS_DRAFT.equals(version.getVersionStatus())) {
            throw new BizException(ErrorCode.VERSION_STATUS_INVALID, "只能发布草稿版本");
        }

        LocalDateTime now = LocalDateTime.now();
        scriptVersionMapper.publish(versionId, scriptId, now, now, null);
        scriptMapper.updateCurrentVersionAndStatus(scriptId, ScriptConstants.STATUS_PUBLISHED, versionId, now, null);

        ScriptVersionEntity publishedVersion = getExistingVersion(scriptId, versionId);
        return toResponse(publishedVersion);
    }

    private ScriptEntity getExistingScript(String scriptId) {
        ScriptEntity script = scriptMapper.selectById(scriptId);
        if (script == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "脚本不存在");
        }
        return script;
    }

    private ScriptVersionEntity getExistingVersion(String scriptId, String versionId) {
        ScriptVersionEntity version = scriptVersionMapper.selectByScriptIdAndId(scriptId, versionId);
        if (version == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "脚本版本不存在");
        }
        return version;
    }

    private ScriptVersionResponse toResponse(ScriptVersionEntity version) {
        ScriptVersionResponse response = new ScriptVersionResponse();
        response.setId(version.getId());
        response.setScriptId(version.getScriptId());
        response.setVersionNo(version.getVersionNo());
        response.setVersionStatus(version.getVersionStatus());
        response.setDescription(version.getDescription());
        response.setPublishedAt(version.getPublishedAt());
        response.setCreatedTime(version.getCreatedTime());
        response.setUpdatedTime(version.getUpdatedTime());
        return response;
    }
}
