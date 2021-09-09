package com.rsosor.app.model.enums;

/**
 * Post Permalink type enum.
 *
 * @author RsosoR
 * @date 2021/9/9
 */
public enum PostPermalinkType implements IValueEnum<Integer> {

    /**
     * /archives/${slug}
     */
    DEFAULT(0),

    /**
     * /1970/01/01/${slug}
     */
    DATE(1),

    /**
     * /1970/01/${slug}
     */
    DAY(2),

    /**
     * /?p=${id}
     */
    ID(3),

    /**
     * /1970/${slug}
     */
    YEAR(4),

    /**
     * archives/${id}
     */
    ID_SLUG(5);

    private final Integer value;

    PostPermalinkType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
