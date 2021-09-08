package com.rsosor.app.model.enums;

/**
 * MFAType
 *
 * @author RsosoR
 * @date 2021/9/2
 */
public enum MFAType implements IValueEnum<Integer> {

    /**
     * Disable MFA auth.
     */
    NONE(0),

    /**
     * Time-based One-time Password (rfc6238).
     * see: https://tools.ietf.org/html/rfc6238
     */
    TFA_TOTP(1);

    private final Integer value;

    MFAType(Integer value) {
        this.value = value;
    }

    public static boolean useMFA(MFAType mfaType) {
        return mfaType != null && MFAType.NONE != mfaType;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
