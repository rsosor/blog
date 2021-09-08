package com.rsosor.app.model.enums;

/**
 * OptionType
 *
 * @author RsosoR
 * @date 2021/9/2
 */
public enum OptionType implements IValueEnum<Integer> {

    /**
     * internal option
     */
    INTERNAL(0),

    /**
     * custom option
     */
    CUSTOM(1);

    private final Integer value;

    OptionType(Integer value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
