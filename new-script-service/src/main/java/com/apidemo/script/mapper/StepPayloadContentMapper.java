package com.apidemo.script.mapper;

import com.apidemo.script.entity.StepPayloadContentEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Data access mapper for step_payload_content table.
 */
@Mapper
public interface StepPayloadContentMapper {

    /**
     * Inserts a payload content row.
     *
     * @param content payload content entity
     * @return affected row count
     */
    int insert(StepPayloadContentEntity content);

    /**
     * Finds an active payload content row by step, direction, and location.
     *
     * @param stepId step id
     * @param direction payload direction
     * @param location payload location
     * @return payload content entity, or null when absent
     */
    StepPayloadContentEntity selectByStepDirectionLocation(@Param("stepId") String stepId,
                                                           @Param("direction") String direction,
                                                           @Param("location") String location);

    /**
     * Lists active payload content rows for a step.
     *
     * @param stepId step id
     * @return payload content rows
     */
    List<StepPayloadContentEntity> listByStepId(@Param("stepId") String stepId);

    /**
     * Updates an active payload content row by primary key.
     *
     * @param content payload content entity carrying updated values
     * @return affected row count
     */
    int update(StepPayloadContentEntity content);
}
