package com.apidemo.script.service;

import com.apidemo.script.dto.ImportPreviewResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
class ImportParserTests {

    @Autowired
    private ImportService importService;

    @Test
    void sampleHarShouldParseRequestMethodUrlHeaderQueryAndBodyFields() throws Exception {
        ImportPreviewResponse preview = importService.preview(file("sample.har"), null);

        assertEquals("HAR", preview.getImportType());
        assertFalse(preview.getSteps().isEmpty());
        assertEquals("POST", preview.getSteps().get(0).getRequestMethod());
        assertEquals("http://localhost:8080/login?tenant=demo", preview.getSteps().get(0).getRequestUrl());
        assertFalse(preview.getSteps().get(0).getFields().stream()
                .noneMatch(field -> "REQUEST_HEADER".equals(field.getFieldScope())));
        assertFalse(preview.getSteps().get(0).getFields().stream()
                .noneMatch(field -> "REQUEST_QUERY".equals(field.getFieldScope())));
        assertFalse(preview.getSteps().get(0).getFields().stream()
                .noneMatch(field -> "REQUEST_BODY".equals(field.getFieldScope())
                        && "$.username".equals(field.getFieldPath())));
    }

    @Test
    void samplePostmanShouldParseRequestMethodUrlHeaderQueryAndBodyFields() throws Exception {
        ImportPreviewResponse preview = importService.preview(file("sample-postman-collection.json"), null);

        assertEquals("POSTMAN", preview.getImportType());
        assertFalse(preview.getSteps().isEmpty());
        assertEquals("POST", preview.getSteps().get(0).getRequestMethod());
        assertEquals("http://localhost:8080/login?tenant=demo", preview.getSteps().get(0).getRequestUrl());
        assertFalse(preview.getSteps().get(0).getFields().stream()
                .noneMatch(field -> "REQUEST_HEADER".equals(field.getFieldScope())));
        assertFalse(preview.getSteps().get(0).getFields().stream()
                .noneMatch(field -> "REQUEST_QUERY".equals(field.getFieldScope())));
        assertFalse(preview.getSteps().get(0).getFields().stream()
                .noneMatch(field -> "REQUEST_BODY".equals(field.getFieldScope())
                        && "$.password".equals(field.getFieldPath())));
    }

    private MockMultipartFile file(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return new MockMultipartFile("file", path, "application/json", resource.getInputStream());
    }
}
