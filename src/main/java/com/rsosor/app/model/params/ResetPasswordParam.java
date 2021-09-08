package com.rsosor.app.model.params;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * ResetPasswordParam
 *
 * @author RsosoR
 * @date 2021/9/3
 */
@Data
public class ResetPasswordParam {

    @NotBlank(message = "用戶名不能為空")
    private String username;

    @NotBlank(message = "信箱不能為空")
    private String email;

    private String code;

    private String password;
}
