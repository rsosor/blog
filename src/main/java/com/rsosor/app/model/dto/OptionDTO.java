package com.rsosor.app.model.dto;

import com.rsosor.app.model.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OptionDTO
 *
 * @author RsosoR
 * @date 2021/9/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionDTO implements IOutputConverter<OptionDTO, Option>{

    private String key;

    private String value;

}
