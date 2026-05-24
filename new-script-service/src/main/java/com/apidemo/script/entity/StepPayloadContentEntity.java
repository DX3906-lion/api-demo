package com.apidemo.script.entity;

import com.apidemo.common.model.BaseEntity;

/**
 * Step payload content entity mapped to the step_payload_content table.
 */
public class StepPayloadContentEntity extends BaseEntity {

    /** Owning step id. */
    private String stepId;

    /** Payload direction, REQUEST or RESPONSE. */
    private String direction;

    /** Payload location, such as BODY, HEADER, QUERY, COOKIE, or FORM. */
    private String location;

    /** Content format, such as JSON, XML, FORM, KEY_VALUE, or TEXT. */
    private String contentFormat;

    /** Raw user-visible or imported content. */
    private String rawContent;

    /** Parsed structured content JSON. */
    private String parsedContentJson;

    /** Hash of the stored content, used for change detection. */
    private String contentHash;

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
}
