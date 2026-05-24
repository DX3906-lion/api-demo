package com.apidemo.script.mapper;

import com.apidemo.script.dto.ScriptFieldDefaultResponse;
import com.apidemo.script.entity.ScriptFieldDefaultEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for script_field_default table.
 */
@Mapper
public interface ScriptFieldDefaultMapper {

    /**
     * Inserts a script field default row.
     *
     * @param fieldDefault script field default entity to persist
     * @return affected row count
     */
    int insert(ScriptFieldDefaultEntity fieldDefault);

    /**
     * Finds a default row by version and field id, including logically deleted rows for upsert.
     *
     * @param versionId owning script version id
     * @param fieldConfigId field configuration id
     * @return default entity, or null when no row exists
     */
    ScriptFieldDefaultEntity selectByVersionAndFieldConfigId(@Param("versionId") String versionId,
                                                             @Param("fieldConfigId") String fieldConfigId);

    /**
     * Updates or reactivates a default row.
     *
     * @param fieldDefault default entity carrying updated values
     * @return affected row count
     */
    int update(ScriptFieldDefaultEntity fieldDefault);

    /**
     * Lists field metadata with active default values for a script version.
     *
     * @param scriptId owning script id
     * @param versionId owning script version id
     * @param stepId optional step filter
     * @param fieldScope optional field scope filter
     * @return field default response list
     */
    List<ScriptFieldDefaultResponse> listDefaults(@Param("scriptId") String scriptId,
                                                  @Param("versionId") String versionId,
                                                  @Param("stepId") String stepId,
                                                  @Param("fieldScope") String fieldScope);

    /**
     * Logically deletes active defaults for a field.
     *
     * @param fieldConfigId field configuration id
     * @param updatedTime update time
     * @param updatedBy updater id
     * @return affected row count
     */
    int logicalDeleteByFieldConfigId(@Param("fieldConfigId") String fieldConfigId,
                                     @Param("updatedTime") LocalDateTime updatedTime,
                                     @Param("updatedBy") String updatedBy);
}
