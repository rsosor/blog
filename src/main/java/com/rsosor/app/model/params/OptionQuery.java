package com.rsosor.app.model.params;

import com.rsosor.app.model.enums.OptionType;
import lombok.Data;

/**
 * OptionQuery
 *
 * @author RsosoR
 * @date 2021/9/3
 */
@Data
public class OptionQuery {

    private String keyword;

    private OptionType type;
}
