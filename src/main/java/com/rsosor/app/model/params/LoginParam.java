package com.rsosor.app.model.params;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * LoginParam
 *
 * @author RsosoR
 * @date 2021/9/3
 */
@Data
@ToString
public class LoginParam {

    @NotBlank(message = "用戶名或信箱不能為空")
    @Size(max = 255, message = "用戶名或信箱的字符長度不超過 {max}")
    private String username;

    @NotBlank(message = "登入密碼不能為空")
    @Size(max = 100, message = "用戶密碼字符長度不能超過 {max}")
    private String password;

    @Size(min = 6, max = 6, message = "兩步驗證碼應為 {max} 位")
    private String authcode;
}
