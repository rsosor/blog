package com.rsosor.app.model.enums;

/**
 * AttachmentType
 * 
 * @author RsosoR
 * @date 2021/9/14
 */
public enum AttachmentType implements IValueEnum<Integer> {

    /**
     * 伺服器
     */
    LOCAL(0),

    /**
     * 又拍雲
     */
    UPOSS(1),

    /**
     * 七牛雲
     */
    QINIUOSS(2),

    /**
     * sm.ms
     */
    SMMS(3),

    /**
     * 阿里雲
     */
    ALIOSS(4),

    /**
     * 百度雲
     */
    BAIDUBOS(5),

    /**
     * 騰訊雲
     */
    TENCENTCOS(6),

    /**
     * 華為雲
     */
    HUAWEIOBS(7),

    /**
     * MINIO
     */
    MINIO(8);

    private final Integer value;

    AttachmentType(Integer value) {
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
