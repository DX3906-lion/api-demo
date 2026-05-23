package com.apidemo.common;

import com.apidemo.common.error.ErrorCode;
import com.apidemo.common.exception.BizException;
import com.apidemo.common.response.ApiResponse;
import com.apidemo.common.response.PageResponse;
import com.apidemo.common.util.IdGenerator;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommonCoreTests {

    @Test
    void apiResponseSuccessShouldWork() {
        ApiResponse<String> response = ApiResponse.success("ok");
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals("000000", response.getCode());
        Assertions.assertEquals("ok", response.getData());
        Assertions.assertNotNull(response.getTimestamp());
    }

    @Test
    void apiResponseFailShouldWork() {
        ApiResponse<Object> response = ApiResponse.fail(ErrorCode.PARAM_INVALID);
        Assertions.assertFalse(response.isSuccess());
        Assertions.assertEquals("400001", response.getCode());
    }

    @Test
    void pageResponseShouldCreate() {
        PageResponse<String> page = new PageResponse<String>(Arrays.asList("a", "b"), 2L, 1L, 10L);
        Assertions.assertEquals(2L, page.getTotal());
        Assertions.assertEquals(1L, page.getPageNo());
        Assertions.assertEquals(10L, page.getPageSize());
        Assertions.assertEquals(2, page.getRecords().size());
    }

    @Test
    void errorCodeShouldExposeFields() {
        Assertions.assertEquals("000000", ErrorCode.SUCCESS.getCode());
        Assertions.assertEquals("success", ErrorCode.SUCCESS.getMessage());
    }

    @Test
    void bizExceptionConstructorShouldWork() {
        BizException exception = new BizException(ErrorCode.BUSINESS_CONFLICT, "conflict");
        Assertions.assertEquals("409001", exception.getCode());
        Assertions.assertEquals("conflict", exception.getMessage());
    }

    @Test
    void idGeneratorShouldReturnNonEmpty() {
        String id = IdGenerator.nextId();
        Assertions.assertNotNull(id);
        Assertions.assertFalse(id.isEmpty());
    }
}
