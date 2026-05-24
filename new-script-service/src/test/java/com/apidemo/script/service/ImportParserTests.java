package com.apidemo.script.service;

import com.apidemo.script.dto.ImportPreviewResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Sql(statements = {
        "DELETE FROM import_log",
        "DELETE FROM raw_import_file",
        "DELETE FROM script_version",
        "DELETE FROM script"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ImportParserTests {

    @Autowired
    private ImportService importService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void sampleHarShouldParseRequestMethodUrlHeaderQueryAndBodyFields() throws Exception {
        ImportPreviewResponse preview = importService.preview(file("sample.har"), null);

        assertNotNull(preview.getImportFileId());
        assertEquals(Boolean.FALSE, preview.getFormalScriptDataCreated());
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

        assertNotNull(preview.getImportFileId());
        assertEquals(Boolean.FALSE, preview.getFormalScriptDataCreated());
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

    @Test
    void previewPersistsRawImportFileAndPreviewLogWithoutFormalScriptRows() throws Exception {
        ImportPreviewResponse preview = importService.preview(file("sample.har"), "HAR");

        assertNotNull(preview.getImportFileId());
        assertEquals(Boolean.FALSE, preview.getFormalScriptDataCreated());
        assertEquals("PREVIEWED", jdbcTemplate.queryForObject(
                "SELECT status FROM raw_import_file WHERE id = ?", String.class, preview.getImportFileId()));
        assertEquals(Integer.valueOf(1), jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM import_log WHERE raw_import_file_id = ? AND stage = 'PREVIEW'",
                Integer.class, preview.getImportFileId()));
        assertEquals(Integer.valueOf(0), jdbcTemplate.queryForObject("SELECT COUNT(1) FROM script", Integer.class));
        assertEquals(Integer.valueOf(0), jdbcTemplate.queryForObject("SELECT COUNT(1) FROM script_version", Integer.class));
    }

    private MockMultipartFile file(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return new MockMultipartFile("file", path, "application/json", resource.getInputStream());
    }
}
