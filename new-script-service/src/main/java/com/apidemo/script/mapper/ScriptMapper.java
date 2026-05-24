package com.apidemo.script.mapper;

import com.apidemo.script.entity.ScriptEntity;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for script table.
 */
@Mapper
public interface ScriptMapper {

    /**
     * Inserts a script row.
     *
     * @param script script entity to persist
     * @return affected row count
     */
    int insert(ScriptEntity script);

    /**
     * Finds an active script by id.
     *
     * @param id script id
     * @return script entity, or null when absent
     */
    ScriptEntity selectById(@Param("id") String id);

    /**
     * Updates the current version pointer and lifecycle status.
     *
     * @param id script id
     * @param status new script status
     * @param currentVersionId current version id
     * @param updatedTime update time
     * @param updatedBy updater id
     * @return affected row count
     */
    int updateCurrentVersionAndStatus(@Param("id") String id,
                                      @Param("status") String status,
                                      @Param("currentVersionId") String currentVersionId,
                                      @Param("updatedTime") LocalDateTime updatedTime,
                                      @Param("updatedBy") String updatedBy);
}
