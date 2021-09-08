package com.rsosor.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * User login env.
 *
 * @author RsosoR
 * @version 1.0
 * @date 2021/9/3
 */
@Data
@ToString
@AllArgsConstructor
public class LoginPreCheckDTO {

    private boolean needMFACode;

}
