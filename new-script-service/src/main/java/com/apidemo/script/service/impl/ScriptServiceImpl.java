package com.apidemo.script.service.impl;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.common.util.IdGenerator;
import com.apidemo.script.constant.ScriptConstants;
import com.apidemo.script.dto.ScriptCreateRequest;
import com.apidemo.script.dto.ScriptDetailResponse;
import com.apidemo.script.dto.ScriptVersionResponse;
import com.apidemo.script.entity.ScriptEntity;
import com.apidemo.script.entity.ScriptVersionEntity;
import com.apidemo.script.mapper.ScriptMapper;
import com.apidemo.script.mapper.ScriptVersionMapper;
import com.apidemo.script.service.ScriptService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default script service implementation.
 */
@Service
public class ScriptServiceImpl implements ScriptService {

    private final ScriptMapper scriptMapper;
    private final ScriptVersionMapper scriptVersionMapper;

    public ScriptServiceImpl(ScriptMapper scriptMapper, ScriptVersionMapper scriptVersionMapper) {
        this.scriptMapper = scriptMapper;
        this.scriptVersionMapper = scriptVersionMapper;
    }

    /**
     * Creates a script and the default draft version atomically.
     *
     * @param request script creation request
     * @return created script detail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScriptDetailResponse createScript(ScriptCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String scriptId = IdGenerator.nextId();
        String versionId = IdGenerator.nextId();

        ScriptEntity script = new ScriptEntity();
        script.setId(scriptId);
        script.setName(request.getName());
        script.setDescription(request.getDescription());
        script.setStatus(ScriptConstants.STATUS_DRAFT);
        script.setCurrentVersionId(versionId);
        script.setCreatedTime(now);
        script.setUpdatedTime(now);
        script.setDeleted(ScriptConstants.NOT_DELETED);
        scriptMapper.insert(script);

        ScriptVersionEntity version = new ScriptVersionEntity();
        version.setId(versionId);
        version.setScriptId(scriptId);
        version.setVersionNo(1);
        version.setVersionStatus(ScriptConstants.STATUS_DRAFT);
        version.setDescription(request.getDescription());
        version.setCreatedTime(now);
        version.setUpdatedTime(now);
        version.setDeleted(ScriptConstants.NOT_DELETED);
        scriptVersionMapper.insert(version);

        return toScriptDetail(script, version);
    }

    /**
     * Gets script detail and loads its current version.
     *
     * @param scriptId script id
     * @return script detail
     */
    @Override
    public ScriptDetailResponse getScriptDetail(String scriptId) {
        ScriptEntity script = getExistingScript(scriptId);
        ScriptVersionEntity currentVersion = scriptVersionMapper.selectByScriptIdAndId(script.getId(), script.getCurrentVersionId());
        return toScriptDetail(script, currentVersion);
    }

    private ScriptEntity getExistingScript(String scriptId) {
        ScriptEntity script = scriptMapper.selectById(scriptId);
        if (script == null) {
            throw new BizException(ErrorCode.RESOURCE_NOT_FOUND, "脚本不存在");
        }
        return script;
    }

    private ScriptDetailResponse toScriptDetail(ScriptEntity script, ScriptVersionEntity version) {
        ScriptDetailResponse response = new ScriptDetailResponse();
        response.setId(script.getId());
        response.setName(script.getName());
        response.setDescription(script.getDescription());
        response.setStatus(script.getStatus());
        response.setCurrentVersionId(script.getCurrentVersionId());
        response.setCreatedTime(script.getCreatedTime());
        response.setUpdatedTime(script.getUpdatedTime());
        response.setCurrentVersion(toVersionResponse(version));
        return response;
    }

    private ScriptVersionResponse toVersionResponse(ScriptVersionEntity version) {
        if (version == null) {
            return null;
        }
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
