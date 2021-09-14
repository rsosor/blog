package com.rsosor.app.model.enums;

/**
 * PostEditorType
 *
 * @author RsosoR
 * @date 2021/9/14
 */
public enum PostEditorType implements IValueEnum<Integer> {

    /**
     * Markdown editor.
     */
    MARKDOWN(0),

    /**
     * Rich text editor.
     */
    RICHTEXT(1);

    private final Integer value;

    PostEditorType(Integer value) {
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
