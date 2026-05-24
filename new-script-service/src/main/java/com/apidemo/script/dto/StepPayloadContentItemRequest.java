package com.apidemo.script.dto;

import javax.validation.constraints.NotBlank;

/**
 * Single payload content item in a save request.
 */
public class StepPayloadContentItemRequest {

    /** Payload direction, REQUEST or RESPONSE. */
    @NotBlank(message = "Payload 方向不能为空")
    private String direction;

    /** Payload location, such as BODY, HEADER, QUERY, COOKIE, or FORM. */
    @NotBlank(message = "Payload 位置不能为空")
    private String location;

    /** Content format, such as JSON, XML, FORM, KEY_VALUE, or TEXT. */
    private String contentFormat;

    /** Raw content text. */
    private String rawContent;

    /** Parsed structured content JSON. */
    private String parsedContentJson;

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
}
