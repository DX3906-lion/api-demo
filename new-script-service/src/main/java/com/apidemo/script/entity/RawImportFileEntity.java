package com.apidemo.script.entity;

import com.apidemo.common.model.BaseEntity;

/**
 * Raw import file entity mapped to raw_import_file for import traceability.
 */
public class RawImportFileEntity extends BaseEntity {

    /** Import type, HAR or POSTMAN. */
    private String importType;

    /** Original uploaded file name. */
    private String originalFileName;

    /** SHA-256 hash of the uploaded file content. */
    private String fileHash;

    /** Uploaded file size in bytes. */
    private Long fileSize;

    /** Character set used to decode the stored content. */
    private String charset;

    /** Original uploaded file text content. */
    private String content;

    /** Raw file lifecycle status such as PREVIEWED or CONFIRMED. */
    private String status;

    /** Script id linked after confirmation. */
    private String confirmedScriptId;

    /** Script version id linked after confirmation. */
    private String confirmedVersionId;

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfirmedScriptId() {
        return confirmedScriptId;
    }

    public void setConfirmedScriptId(String confirmedScriptId) {
        this.confirmedScriptId = confirmedScriptId;
    }

    public String getConfirmedVersionId() {
        return confirmedVersionId;
    }

    public void setConfirmedVersionId(String confirmedVersionId) {
        this.confirmedVersionId = confirmedVersionId;
    }
}
