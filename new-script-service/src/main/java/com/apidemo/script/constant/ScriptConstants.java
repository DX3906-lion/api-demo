package com.apidemo.script.constant;

/**
 * Script service status and default value constants.
 */
public final class ScriptConstants {

    /** Draft status, editable before publish. */
    public static final String STATUS_DRAFT = "DRAFT";

    /** Published status, read-only for step definitions. */
    public static final String STATUS_PUBLISHED = "PUBLISHED";

    /** HTTP step type used by imported API requests. */
    public static final String STEP_TYPE_HTTP = "HTTP";

    /** HTTP protocol type used by API request configuration. */
    public static final String PROTOCOL_HTTP = "HTTP";

    /** Default JSON object used when optional step config is absent. */
    public static final String EMPTY_JSON_OBJECT = "{}";

    /** Enabled flag value. */
    public static final Integer ENABLED = 1;

    /** Disabled flag value. */
    public static final Integer DISABLED = 0;

    /** Active logical delete flag. */
    public static final Integer NOT_DELETED = 0;

    /** Deleted logical delete flag. */
    public static final Integer DELETED = 1;

    /** Enabled business status used by case data sets. */
    public static final String STATUS_ENABLED = "ENABLED";

    /** Manual value source used by field defaults and case field overrides. */
    public static final String VALUE_SOURCE_MANUAL = "MANUAL";

    /** Import value source used by imported field defaults. */
    public static final String VALUE_SOURCE_IMPORT = "IMPORT";

    /** HAR import type. */
    public static final String IMPORT_TYPE_HAR = "HAR";

    /** Postman collection import type. */
    public static final String IMPORT_TYPE_POSTMAN = "POSTMAN";

    /** Request header field scope. */
    public static final String FIELD_SCOPE_REQUEST_HEADER = "REQUEST_HEADER";

    /** Request query field scope. */
    public static final String FIELD_SCOPE_REQUEST_QUERY = "REQUEST_QUERY";

    /** Request body field scope. */
    public static final String FIELD_SCOPE_REQUEST_BODY = "REQUEST_BODY";

    /** Request direction for payload content rows. */
    public static final String PAYLOAD_DIRECTION_REQUEST = "REQUEST";

    /** Response direction for payload content rows. */
    public static final String PAYLOAD_DIRECTION_RESPONSE = "RESPONSE";

    /** Body payload location. */
    public static final String PAYLOAD_LOCATION_BODY = "BODY";

    /** Header payload location. */
    public static final String PAYLOAD_LOCATION_HEADER = "HEADER";

    /** Query payload location. */
    public static final String PAYLOAD_LOCATION_QUERY = "QUERY";

    /** Cookie payload location. */
    public static final String PAYLOAD_LOCATION_COOKIE = "COOKIE";

    /** Form payload location. */
    public static final String PAYLOAD_LOCATION_FORM = "FORM";

    /** JSON content format. */
    public static final String CONTENT_FORMAT_JSON = "JSON";

    /** XML content format. */
    public static final String CONTENT_FORMAT_XML = "XML";

    /** Form content format. */
    public static final String CONTENT_FORMAT_FORM = "FORM";

    /** Key-value content format. */
    public static final String CONTENT_FORMAT_KEY_VALUE = "KEY_VALUE";

    /** Text content format. */
    public static final String CONTENT_FORMAT_TEXT = "TEXT";

    /** Follow redirect flag enabled. */
    public static final String FOLLOW_REDIRECT_TRUE = "1";

    /** String field data type. */
    public static final String DATA_TYPE_STRING = "STRING";

    /** Number field data type. */
    public static final String DATA_TYPE_NUMBER = "NUMBER";

    /** Boolean field data type. */
    public static final String DATA_TYPE_BOOLEAN = "BOOLEAN";

    /** Object field data type. */
    public static final String DATA_TYPE_OBJECT = "OBJECT";

    /** Array field data type. */
    public static final String DATA_TYPE_ARRAY = "ARRAY";

    private ScriptConstants() {
    }
}
