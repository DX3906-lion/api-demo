package com.apidemo.script.mapper;

import com.apidemo.script.entity.RawImportFileEntity;
import java.time.LocalDateTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for raw_import_file table.
 */
@Mapper
public interface RawImportFileMapper {

    /**
     * Inserts a raw import file row.
     *
     * @param rawImportFile raw import file entity
     * @return affected row count
     */
    int insert(RawImportFileEntity rawImportFile);

    /**
     * Finds an active raw import file by id.
     *
     * @param id raw import file id
     * @return raw import file entity, or null when absent
     */
    RawImportFileEntity selectById(@Param("id") String id);

    /**
     * Marks a raw import file as confirmed and links generated script metadata.
     *
     * @param id raw import file id
     * @param confirmedScriptId confirmed script id
     * @param confirmedVersionId confirmed script version id
     * @param status new raw file status
     * @param updatedTime update time
     * @param updatedBy updater id
     * @return affected row count
     */
    int markConfirmed(@Param("id") String id,
                      @Param("confirmedScriptId") String confirmedScriptId,
                      @Param("confirmedVersionId") String confirmedVersionId,
                      @Param("status") String status,
                      @Param("updatedTime") LocalDateTime updatedTime,
                      @Param("updatedBy") String updatedBy);
}
