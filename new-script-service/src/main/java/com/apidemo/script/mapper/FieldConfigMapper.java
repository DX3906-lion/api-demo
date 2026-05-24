package com.apidemo.script.mapper;

import com.apidemo.script.entity.FieldConfigEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for field_config table.
 */
@Mapper
public interface FieldConfigMapper {

    /**
     * Inserts a field configuration row.
     *
     * @param fieldConfig field configuration entity to persist
     * @return affected row count
     */
    int insert(FieldConfigEntity fieldConfig);

    /**
     * Finds an active field configuration by script, version, and field id.
     *
     * @param scriptId owning script id
     * @param versionId owning script version id
     * @param id field configuration id
     * @return field configuration entity, or null when absent
     */
    FieldConfigEntity selectByScriptVersionAndId(@Param("scriptId") String scriptId,
                                                 @Param("versionId") String versionId,
                                                 @Param("id") String id);

    /**
     * Lists active field configurations with optional filters.
     *
     * @param scriptId owning script id
     * @param versionId owning script version id
     * @param stepId optional step filter
     * @param fieldScope optional field scope filter
     * @return ordered field configuration list
     */
    List<FieldConfigEntity> listByVersion(@Param("scriptId") String scriptId,
                                          @Param("versionId") String versionId,
                                          @Param("stepId") String stepId,
                                          @Param("fieldScope") String fieldScope);

    /**
     * Counts active field configurations with the same stable key in one version.
     *
     * @param versionId owning script version id
     * @param stableFieldKey stable field key
     * @param excludeId field id to exclude during update, may be null
     * @return matching active field count
     */
    int countByStableFieldKey(@Param("versionId") String versionId,
                              @Param("stableFieldKey") String stableFieldKey,
                              @Param("excludeId") String excludeId);

    /**
     * Updates editable fields on an active field configuration.
     *
     * @param fieldConfig field configuration entity carrying updated values
     * @return affected row count
     */
    int update(FieldConfigEntity fieldConfig);

    /**
     * Logically deletes an active field configuration.
     *
     * @param id field configuration id
     * @param scriptId owning script id
     * @param versionId owning script version id
     * @param updatedTime update time
     * @param updatedBy updater id
     * @return affected row count
     */
    int logicalDelete(@Param("id") String id,
                      @Param("scriptId") String scriptId,
                      @Param("versionId") String versionId,
                      @Param("updatedTime") LocalDateTime updatedTime,
                      @Param("updatedBy") String updatedBy);
}
