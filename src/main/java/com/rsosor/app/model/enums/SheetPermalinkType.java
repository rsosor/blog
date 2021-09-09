package com.rsosor.app.model.enums;

/**
 * Sheet Permalink type enum.
 *
 * @author RsosoR
 * @date 2021/9/9
 */
public enum SheetPermalinkType implements IValueEnum<Integer> {

    /**
     * {@link com.rsosor.app.model.properties.PermalinkProperties#SHEET_PREFIX}/${slug}
     */
    SECONDARY(0),

    /**
     * /${slug}
     */
    ROOT(1);

    private final Integer value;

    SheetPermalinkType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
