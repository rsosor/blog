package com.rsosor.app.model.properties;

import static com.rsosor.app.model.support.RsosoRConst.DEFAULT_THEME_ID;

/**
 * PrimaryProperties
 *
 * @author RsosoR
 * @date 2021/9/2
 */
public enum PrimaryProperties implements IPropertyEnum {

    /**
     * is blog installed.
     */
    IS_INSTALLED("is_installed", Boolean.class, "false"),

    /**
     * current actived theme.
     */
    THEME("theme", String.class, DEFAULT_THEME_ID),

    /**
     * blog birthday
     */
    BIRTHDAY("birthday", Long.class, ""),

    /**
     * developer mode.
     */
    DEV_MODE("developer_mode", Boolean.class, "false"),

    /**
     * default menu team name
     */
    DEFAULT_MENU_TEAM("default_menu_team", String.class, "");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    PrimaryProperties(String value, Class<?> type, String defaultValue) {
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
