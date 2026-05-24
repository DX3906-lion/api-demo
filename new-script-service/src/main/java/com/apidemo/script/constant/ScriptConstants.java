package com.apidemo.script.constant;

/**
 * Script service status and default value constants.
 */
public final class ScriptConstants {

    /** Draft status, editable before publish. */
    public static final String STATUS_DRAFT = "DRAFT";

    /** Published status, read-only for step definitions. */
    public static final String STATUS_PUBLISHED = "PUBLISHED";

    /** Default JSON object used when optional step config is absent. */
    public static final String EMPTY_JSON_OBJECT = "{}";

    /** Enabled flag value. */
    public static final Integer ENABLED = 1;

    /** Active logical delete flag. */
    public static final Integer NOT_DELETED = 0;

    private ScriptConstants() {
    }
}
