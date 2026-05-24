package com.apidemo.script.mapper;

import com.apidemo.script.dto.CaseFieldValueResponse;
import com.apidemo.script.entity.CaseFieldValueEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for case_field_value table.
 */
@Mapper
public interface CaseFieldValueMapper {

    /**
     * Inserts a case field value row.
     *
     * @param value case field value entity to persist
     * @return affected row count
     */
    int insert(CaseFieldValueEntity value);

    /**
     * Finds a case field value by case data set and field id, including deleted rows for upsert.
     *
     * @param caseDataSetId case data set id
     * @param fieldConfigId field configuration id
     * @return case field value entity, or null when no row exists
     */
    CaseFieldValueEntity selectByCaseAndFieldConfigId(@Param("caseDataSetId") String caseDataSetId,
                                                      @Param("fieldConfigId") String fieldConfigId);

    /**
     * Updates or reactivates a case field value row.
     *
     * @param value case field value entity carrying updated values
     * @return affected row count
     */
    int update(CaseFieldValueEntity value);

    /**
     * Lists active field override values with field metadata.
     *
     * @param caseDataSetId case data set id
     * @return field override value list
     */
    List<CaseFieldValueResponse> listByCaseDataSetId(@Param("caseDataSetId") String caseDataSetId);

    /**
     * Logically deletes active field values under a case data set.
     *
     * @param caseDataSetId case data set id
     * @param updatedTime update time
     * @param updatedBy updater id
     * @return affected row count
     */
    int logicalDeleteByCaseDataSetId(@Param("caseDataSetId") String caseDataSetId,
                                     @Param("updatedTime") LocalDateTime updatedTime,
                                     @Param("updatedBy") String updatedBy);
}
