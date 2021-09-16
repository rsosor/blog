package com.rsosor.app.config.attributeconverter;

import com.rsosor.app.model.enums.IValueEnum;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 * AttributeConverterInterceptor
 *
 * @author RsosoR
 * @date 2021/9/16
 */
public class AttributeConverterInterceptor {

    private AttributeConverterInterceptor() {}

    @RuntimeType
    public static <T extends Enum<T> & IValueEnum<V>, V> V convertToDatabaseColumn(T attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @RuntimeType
    public static <T extends Enum<T> & IValueEnum<V>, V> T convertToEntityAttribute(V dbData,
        @FieldValue("enumType") Class<T> enumType) {
        return dbData == null ? null : IValueEnum.valueToEnum(enumType, dbData);
    }
}
