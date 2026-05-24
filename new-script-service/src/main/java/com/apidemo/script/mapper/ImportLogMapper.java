package com.apidemo.script.mapper;

import com.apidemo.script.entity.ImportLogEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for import_log table.
 */
@Mapper
public interface ImportLogMapper {

    /**
     * Inserts an import log row.
     *
     * @param importLog import log entity
     * @return affected row count
     */
    int insert(ImportLogEntity importLog);

    /**
     * Lists active import logs for a raw import file.
     *
     * @param rawImportFileId raw import file id
     * @return import logs ordered by creation time
     */
    List<ImportLogEntity> listByRawImportFileId(@Param("rawImportFileId") String rawImportFileId);
}
