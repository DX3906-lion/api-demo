package com.apidemo.script.dto;

import java.util.List;

/**
 * Response body for case data set details including field overrides.
 */
public class CaseDataSetDetailResponse {

    /** Case data set base information. */
    private CaseDataSetResponse caseDataSet;

    /** Field override value list. */
    private List<CaseFieldValueResponse> values;

    public CaseDataSetResponse getCaseDataSet() {
        return caseDataSet;
    }

    public void setCaseDataSet(CaseDataSetResponse caseDataSet) {
        this.caseDataSet = caseDataSet;
    }

    public List<CaseFieldValueResponse> getValues() {
        return values;
    }

    public void setValues(List<CaseFieldValueResponse> values) {
        this.values = values;
    }
}
