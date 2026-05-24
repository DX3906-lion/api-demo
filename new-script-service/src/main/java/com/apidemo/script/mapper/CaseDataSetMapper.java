package com.apidemo.script.mapper;

import com.apidemo.script.entity.CaseDataSetEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for case_data_set table.
 */
@Mapper
public interface CaseDataSetMapper {

    /**
     * Inserts a case data set row.
     *
     * @param caseDataSet case data set entity to persist
     * @return affected row count
     */
    int insert(CaseDataSetEntity caseDataSet);

    /**
     * Finds an active case data set by script, version, and data set id.
     *
     * @param scriptId owning script id
     * @param versionId owning script version id
     * @param id case data set id
     * @return case data set entity, or null when absent
     */
    CaseDataSetEntity selectByScriptVersionAndId(@Param("scriptId") String scriptId,
                                                 @Param("versionId") String versionId,
                                                 @Param("id") String id);

    /**
     * Lists active case data sets under a script version.
     *
     * @param scriptId owning script id
     * @param versionId owning script version id
     * @return ordered case data set list
     */
    List<CaseDataSetEntity> listByVersion(@Param("scriptId") String scriptId, @Param("versionId") String versionId);

    /**
     * Updates editable case data set fields.
     *
     * @param caseDataSet case data set entity carrying updated values
     * @return affected row count
     */
    int update(CaseDataSetEntity caseDataSet);

    /**
     * Logically deletes a case data set.
     *
     * @param id case data set id
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
