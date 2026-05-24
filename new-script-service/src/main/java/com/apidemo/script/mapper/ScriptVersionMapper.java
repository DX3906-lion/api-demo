package com.apidemo.script.mapper;

import com.apidemo.script.entity.ScriptVersionEntity;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for script_version table.
 */
@Mapper
public interface ScriptVersionMapper {

    /**
     * Inserts a script version row.
     *
     * @param version script version entity to persist
     * @return affected row count
     */
    int insert(ScriptVersionEntity version);

    /**
     * Finds an active version by script id and version id.
     *
     * @param scriptId owning script id
     * @param id version id
     * @return version entity, or null when absent
     */
    ScriptVersionEntity selectByScriptIdAndId(@Param("scriptId") String scriptId, @Param("id") String id);

    /**
     * Finds an active version by id.
     *
     * @param id version id
     * @return version entity, or null when absent
     */
    ScriptVersionEntity selectById(@Param("id") String id);

    /**
     * Counts active DRAFT versions under the script.
     *
     * @param scriptId owning script id
     * @return active DRAFT version count
     */
    int countDraftByScriptId(@Param("scriptId") String scriptId);

    /**
     * Finds the maximum version number under the script.
     *
     * @param scriptId owning script id
     * @return maximum version number, or null when absent
     */
    Integer selectMaxVersionNo(@Param("scriptId") String scriptId);

    /**
     * Publishes a draft version by updating status and publish time.
     *
     * @param id version id
     * @param scriptId owning script id
     * @param publishedAt publish time
     * @param updatedTime update time
     * @param updatedBy updater id
     * @return affected row count
     */
    int publish(@Param("id") String id,
                @Param("scriptId") String scriptId,
                @Param("publishedAt") LocalDateTime publishedAt,
                @Param("updatedTime") LocalDateTime updatedTime,
                @Param("updatedBy") String updatedBy);

    /**
     * Associates a raw import file with a script version.
     *
     * @param id version id
     * @param scriptId owning script id
     * @param rawImportFileId raw import file id
     * @param updatedTime update time
     * @param updatedBy updater id
     * @return affected row count
     */
    int updateRawImportFileId(@Param("id") String id,
                              @Param("scriptId") String scriptId,
                              @Param("rawImportFileId") String rawImportFileId,
                              @Param("updatedTime") LocalDateTime updatedTime,
                              @Param("updatedBy") String updatedBy);
}
