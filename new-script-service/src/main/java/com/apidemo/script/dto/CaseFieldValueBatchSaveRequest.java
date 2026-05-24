package com.apidemo.script.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * Request body for batch saving case field override values.
 */
public class CaseFieldValueBatchSaveRequest {

    /** Field override value items. */
    @Valid
    @NotEmpty(message = "字段覆盖值不能为空")
    private List<CaseFieldValueSaveItem> values;

    public List<CaseFieldValueSaveItem> getValues() {
        return values;
    }

    public void setValues(List<CaseFieldValueSaveItem> values) {
        this.values = values;
    }
}
