package com.rsosor.app.model.properties;

import org.springframework.lang.NonNull;

/**
 * SheetProperties
 *
 * @author RsosoR
 * @date 2021/9/14
 */
public enum SheetProperties implements IPropertyEnum {

    /**
     * Links page title.
     */
    LINKS_TITLE("links_title", String.class, "友情鏈接"),

    /**
     * Photos page title.
     */
    PHOTOS_TITLE("photos_title", String.class, "圖庫"),

    /**
     * Photos page size.
     */
    PHOTOS_PAGE_SIZE("photos_page_size", Integer.class, "10"),

    /**
     * Journals page title.
     */
    JOURNALS_TITLE("journals_title", String.class, "日誌"),

    /**
     * Journals page size.
     */
    JOURNALS_PAGE_SIZE("journals_page_size", Integer.class, "10");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    SheetProperties(String value, Class<?> type, String defaultValue) {
        this.value = value;
        this.type = type;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    @NonNull
    public String defaultValue() {
        return defaultValue;
    }
}
