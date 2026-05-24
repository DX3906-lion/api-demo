package com.apidemo.script.mapper;

import com.apidemo.script.entity.StepDefinitionEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for step_definition table.
 */
@Mapper
public interface StepDefinitionMapper {

    /**
     * Inserts a step definition row.
     *
     * @param step step definition entity to persist
     * @return affected row count
     */
    int insert(StepDefinitionEntity step);

    /**
     * Finds an active step by script id, version id, and step id.
     *
     * @param scriptId owning script id
     * @param versionId owning version id
     * @param id step id
     * @return step entity, or null when absent
     */
    StepDefinitionEntity selectByScriptVersionAndId(@Param("scriptId") String scriptId,
                                                    @Param("versionId") String versionId,
                                                    @Param("id") String id);

    /**
     * Lists active steps in a version ordered by sort number.
     *
     * @param scriptId owning script id
     * @param versionId owning version id
     * @return active step list
     */
    List<StepDefinitionEntity> listByVersion(@Param("scriptId") String scriptId, @Param("versionId") String versionId);

    /**
     * Updates editable fields on an active step.
     *
     * @param step step definition entity carrying updated values
     * @return affected row count
     */
    int update(StepDefinitionEntity step);

    /**
     * Logically deletes an active step.
     *
     * @param id step id
     * @param scriptId owning script id
     * @param versionId owning version id
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
