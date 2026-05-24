package com.apidemo.script.service;

import com.apidemo.script.dto.CaseDataSetDetailResponse;
import com.apidemo.script.dto.CaseDataSetRequest;
import com.apidemo.script.dto.CaseDataSetResponse;
import com.apidemo.script.dto.CaseFieldValueBatchSaveRequest;
import com.apidemo.script.dto.CaseFieldValueResponse;
import java.util.List;

/**
 * Service interface for case data sets and field override values.
 */
public interface CaseDataSetService {

    /**
     * Creates a case data set under a script version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param request case data set request
     * @return created case data set
     */
    CaseDataSetResponse createCaseDataSet(String scriptId, String versionId, CaseDataSetRequest request);

    /**
     * Lists case data sets under a script version.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @return case data set list
     */
    List<CaseDataSetResponse> listCaseDataSets(String scriptId, String versionId);

    /**
     * Gets case data set detail with field override values.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     * @return case data set detail
     */
    CaseDataSetDetailResponse getCaseDataSetDetail(String scriptId, String versionId, String caseDataSetId);

    /**
     * Updates a case data set.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     * @param request case data set request
     * @return updated case data set
     */
    CaseDataSetResponse updateCaseDataSet(String scriptId, String versionId, String caseDataSetId, CaseDataSetRequest request);

    /**
     * Logically deletes a case data set and its field override values.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     */
    void deleteCaseDataSet(String scriptId, String versionId, String caseDataSetId);

    /**
     * Batch saves case field override values.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     * @param request batch save request
     * @return saved field override value list
     */
    List<CaseFieldValueResponse> saveCaseFieldValues(String scriptId, String versionId, String caseDataSetId, CaseFieldValueBatchSaveRequest request);

    /**
     * Lists case field override values.
     *
     * @param scriptId script id
     * @param versionId script version id
     * @param caseDataSetId case data set id
     * @return field override value list
     */
    List<CaseFieldValueResponse> listCaseFieldValues(String scriptId, String versionId, String caseDataSetId);
}
