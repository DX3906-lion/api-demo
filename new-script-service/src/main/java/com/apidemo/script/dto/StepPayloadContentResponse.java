package com.apidemo.script.dto;

import java.time.LocalDateTime;

/**
 * Response body for step payload content.
 */
public class StepPayloadContentResponse {

    /** Payload content row id. */
    private String id;

    /** Owning step id. */
    private String stepId;

    /** Payload direction, REQUEST or RESPONSE. */
    private String direction;

    /** Payload location, such as BODY, HEADER, QUERY, COOKIE, or FORM. */
    private String location;

    /** Content format, such as JSON, XML, FORM, KEY_VALUE, or TEXT. */
    private String contentFormat;

    /** Raw content text. */
    private String rawContent;

    /** Parsed structured content JSON. */
    private String parsedContentJson;

    /** Hash of raw or parsed content. */
    private String contentHash;

    /** Row created time. */
    private LocalDateTime createdTime;

    /** Row updated time. */
    private LocalDateTime updatedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContentFormat() {
        return contentFormat;
    }

    public void setContentFormat(String contentFormat) {
        this.contentFormat = contentFormat;
    }

    public String getRawContent() {
        return rawContent;
    }

    public void setRawContent(String rawContent) {
        this.rawContent = rawContent;
    }

    public String getParsedContentJson() {
        return parsedContentJson;
    }

    public void setParsedContentJson(String parsedContentJson) {
        this.parsedContentJson = parsedContentJson;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}
