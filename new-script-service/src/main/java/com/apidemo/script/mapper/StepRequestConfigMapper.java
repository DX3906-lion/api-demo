package com.apidemo.script.mapper;

import com.apidemo.script.entity.StepRequestConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for step_request_config table.
 */
@Mapper
public interface StepRequestConfigMapper {

    /**
     * Inserts a request configuration row.
     *
     * @param config request configuration entity
     * @return affected row count
     */
    int insert(StepRequestConfigEntity config);

    /**
     * Finds the active request configuration by step id.
     *
     * @param stepId step id
     * @return request configuration entity, or null when absent
     */
    StepRequestConfigEntity selectByStepId(@Param("stepId") String stepId);

    /**
     * Updates an active request configuration row by primary key.
     *
     * @param config request configuration entity carrying updated values
     * @return affected row count
     */
    int update(StepRequestConfigEntity config);
}
