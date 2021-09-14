package com.rsosor.app.model.enums;

/**
 * TimeUnit
 *
 * @author RsosoR
 * @date 2021/9/14
 */
public enum TimeUnit implements IValueEnum<Integer> {

    /**
     * 天
     */
    DAY(0),

    /**
     * 小時
     */
    HOUR(1);

    private final Integer value;

    TimeUnit(Integer value) {
        this.value = value;
    }

    /**
     * Get enum value.
     *
     * @return enum value
     */
    @Override
    public Integer getValue() {
        return value;
    }
}
