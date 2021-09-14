package com.rsosor.app.model.properties;

import com.rsosor.app.model.support.RsosoRConst;

/**
 * AliOssProperties
 *
 * @author RsosoR
 * @date 2021/9/14
 */
public enum AliOssProperties implements IPropertyEnum {

    /**
     * Aliyun oss domain protocol
     */
    OSS_PROTOCOL("oss_ali_domain_protocol", String.class, RsosoRConst.PROTOCOL_HTTPS),

    /**
     * Aliyun oss domain
     */
    OSS_DOMAIN("oss_ali_domain", String.class, ""),

    /**
     * Aliyun oss endpoint.
     */
    OSS_ENDPOINT("oss_ali_endpoint", String.class, ""),

    /**
     * Aliyun oss bucket name.
     */
    OSS_BUCKET_NAME("oss_ali_bucket_name", String.class, ""),

    /**
     * Aliyun oss access key.
     */
    OSS_ACCESS_KEY("oss_ali_access_key", String.class, ""),

    /**
     * Aliyun oss access secret.
     */
    OSS_ACCESS_SECRET("oss_ali_access_secret", String.class, ""),

    /**
     * Aliyun oss source
     */
    OSS_SOURCE("oss_ali_source", String.class, ""),

    /**
     * Aliyun oss style rule.
     */
    OSS_STYLE_RULE("oss_ali_style_rule", String.class, ""),

    /**
     * Aliyun oss thumbnail style rule.
     */
    OSS_THUMBNAIL_STYLE_RULE("oss_ali_thumbnail_style_rule", String.class, "");

    private final String value;

    private final Class<?> type;

    private final String defaultValue;

    AliOssProperties(String value, Class<?> type, String defaultValue) {
        this.defaultValue = defaultValue;
        if (!IPropertyEnum.isSupportedType(type)) {
            throw new IllegalArgumentException("Unsupported blog property type: " + type);
        }

        this.value = value;
        this.type = type;
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
    public String defaultValue() {
        return defaultValue;
    }
}
