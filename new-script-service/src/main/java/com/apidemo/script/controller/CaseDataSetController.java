package com.apidemo.script.controller;

import com.apidemo.common.response.ApiResponse;
import com.apidemo.script.dto.CaseDataSetDetailResponse;
import com.apidemo.script.dto.CaseDataSetRequest;
import com.apidemo.script.dto.CaseDataSetResponse;
import com.apidemo.script.dto.CaseFieldValueBatchSaveRequest;
import com.apidemo.script.dto.CaseFieldValueResponse;
import com.apidemo.script.service.CaseDataSetService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for case data set and case field value operations.
 */
@RestController
@RequestMapping("/api/scripts/{scriptId}/versions/{versionId}/case-data-sets")
public class CaseDataSetController {

    private final CaseDataSetService caseDataSetService;

    public CaseDataSetController(CaseDataSetService caseDataSetService) {
        this.caseDataSetService = caseDataSetService;
    }

    /**
     * Creates a case data set under a script version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param request case data set request
     * @return created case data set response
     */
    @PostMapping
    public ApiResponse<CaseDataSetResponse> createCaseDataSet(@PathVariable String scriptId,
                                                              @PathVariable String versionId,
                                                              @Valid @RequestBody CaseDataSetRequest request) {
        return ApiResponse.success(caseDataSetService.createCaseDataSet(scriptId, versionId, request));
    }

    /**
     * Lists case data sets under a script version.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @return case data set list response
     */
    @GetMapping
    public ApiResponse<List<CaseDataSetResponse>> listCaseDataSets(@PathVariable String scriptId,
                                                                   @PathVariable String versionId) {
        return ApiResponse.success(caseDataSetService.listCaseDataSets(scriptId, versionId));
    }

    /**
     * Gets case data set detail with field override values.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param caseDataSetId case data set id path variable
     * @return case data set detail response
     */
    @GetMapping("/{caseDataSetId}")
    public ApiResponse<CaseDataSetDetailResponse> getCaseDataSetDetail(@PathVariable String scriptId,
                                                                       @PathVariable String versionId,
                                                                       @PathVariable String caseDataSetId) {
        return ApiResponse.success(caseDataSetService.getCaseDataSetDetail(scriptId, versionId, caseDataSetId));
    }

    /**
     * Updates a case data set.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param caseDataSetId case data set id path variable
     * @param request case data set request
     * @return updated case data set response
     */
    @PutMapping("/{caseDataSetId}")
    public ApiResponse<CaseDataSetResponse> updateCaseDataSet(@PathVariable String scriptId,
                                                              @PathVariable String versionId,
                                                              @PathVariable String caseDataSetId,
                                                              @Valid @RequestBody CaseDataSetRequest request) {
        return ApiResponse.success(caseDataSetService.updateCaseDataSet(scriptId, versionId, caseDataSetId, request));
    }

    /**
     * Logically deletes a case data set and its field override values.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param caseDataSetId case data set id path variable
     * @return empty success response
     */
    @DeleteMapping("/{caseDataSetId}")
    public ApiResponse<Void> deleteCaseDataSet(@PathVariable String scriptId,
                                               @PathVariable String versionId,
                                               @PathVariable String caseDataSetId) {
        caseDataSetService.deleteCaseDataSet(scriptId, versionId, caseDataSetId);
        return ApiResponse.success();
    }

    /**
     * Batch saves case field override values.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param caseDataSetId case data set id path variable
     * @param request batch save request
     * @return saved field override values
     */
    @PutMapping("/{caseDataSetId}/values")
    public ApiResponse<List<CaseFieldValueResponse>> saveCaseFieldValues(@PathVariable String scriptId,
                                                                         @PathVariable String versionId,
                                                                         @PathVariable String caseDataSetId,
                                                                         @Valid @RequestBody CaseFieldValueBatchSaveRequest request) {
        return ApiResponse.success(caseDataSetService.saveCaseFieldValues(scriptId, versionId, caseDataSetId, request));
    }

    /**
     * Lists case field override values.
     *
     * @param scriptId script id path variable
     * @param versionId version id path variable
     * @param caseDataSetId case data set id path variable
     * @return field override value list response
     */
    @GetMapping("/{caseDataSetId}/values")
    public ApiResponse<List<CaseFieldValueResponse>> listCaseFieldValues(@PathVariable String scriptId,
                                                                         @PathVariable String versionId,
                                                                         @PathVariable String caseDataSetId) {
        return ApiResponse.success(caseDataSetService.listCaseFieldValues(scriptId, versionId, caseDataSetId));
    }
}
