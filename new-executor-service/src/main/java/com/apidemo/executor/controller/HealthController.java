package com.apidemo.executor.controller;

import com.apidemo.common.response.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("status", "UP");
        data.put("service", "new-executor-service");
        return ApiResponse.success(data);
    }
}
