package com.rsosor.app.model.params;

import com.rsosor.app.model.dto.base.IInputConverter;
import com.rsosor.app.model.entity.Option;
import com.rsosor.app.model.enums.OptionType;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * OptionParam
 *
 * @author RsosoR
 * @date 2021/9/3
 */
@Data
public class OptionParam implements IInputConverter<Option> {

    @NotBlank(message = "Option key must not be blank")
    @Size(max = 100, message = "Length of option key must not be more than {max}")
    private String key;

    private String value;

    private OptionType type;
}
