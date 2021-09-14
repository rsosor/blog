package com.rsosor.app.model.properties;

/**
 * ApiProperties
 *
 * @author RsosoR
 * @date 2021/9/14
 */
public enum ApiProperties implements IPropertyEnum {

    /**
     * api_enabled
     */
    API_ENABLED("api_enabled", Boolean.class, "false"),

    /**
     * api_access_key
     */
    API_ACCESS_KEY("api_access_key", String.class, "");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    ApiProperties(String value, Class<?> type, String defaultValue) {
        this.value = value;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public String defaultValue() {
        return defaultValue;
    }

    @Override
    public String getValue() {
        return value;
    }
}
