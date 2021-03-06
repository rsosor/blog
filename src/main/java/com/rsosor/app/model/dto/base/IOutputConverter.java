package com.rsosor.app.model.dto.base;

import org.springframework.lang.NonNull;

import static com.rsosor.app.utils.BeanUtils.updateProperties;

/**
 * Converter interface for output DTO.
 *
 * <b>The implementation type must be equal to DTO type</b>
 *
 * @param <DtoT> the implementation class type
 * @param <D> domain type
 * @author RsosoR
 * @date 2021/10/4
 */
public interface IOutputConverter<DtoT extends IOutputConverter<DtoT, D>, D> {

    /**
     * Convert from domain.(shallow)
     *
     * @param domain domain data
     * @return converted dto data
     */
    @SuppressWarnings("unchecked")
    @NonNull
    default <T extends DtoT> T convertFrom(@NonNull D domain) {

        updateProperties(domain, this);

        return (T) this;
    }
}
