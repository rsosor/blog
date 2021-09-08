package com.rsosor.app.model.dto;

import com.rsosor.app.model.enums.OptionType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

/**
 * OptionSimpleDTO
 *
 * @author RsosoR
 * @date 2021/9/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OptionSimpleDTO extends OptionDTO {

    private Integer id;

    private OptionType type;

    private Date createTime;

    private Date updateTime;
}
