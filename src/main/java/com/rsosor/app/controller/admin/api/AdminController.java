package com.rsosor.app.controller.admin.api;

import com.rsosor.app.annotation.DisableOnCondition;
import com.rsosor.app.cache.lock.CacheLock;
import com.rsosor.app.model.dto.EnvironmentDTO;
import com.rsosor.app.model.dto.LoginPreCheckDTO;
import com.rsosor.app.model.entity.User;
import com.rsosor.app.model.enums.MFAType;
import com.rsosor.app.model.params.LoginParam;
import com.rsosor.app.model.params.ResetPasswordParam;
import com.rsosor.app.model.properties.PrimaryProperties;
import com.rsosor.app.model.support.BaseResponse;
import com.rsosor.app.security.token.AuthToken;
import com.rsosor.app.service.IAdminService;
import com.rsosor.app.service.IOptionService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * AdminController
 *
 * @author RsosoR
 * @date 2021/9/2
 */

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final IAdminService adminService;

    private final IOptionService optionService;

    public AdminController(IAdminService adminService, IOptionService optionService) {
        this.adminService = adminService;
        this.optionService = optionService;
    }

    @GetMapping(value = "/is_installed")
    @ApiOperation("Checks Installation status")
    public boolean isInstall() {
        return optionService.getByPropertyOrDefault(PrimaryProperties.IS_INSTALLED, Boolean.class,
                false);
    }

    @PostMapping("login/precheck")
    @ApiOperation("Login")
    @CacheLock(autoDelete = false, prefix = "login_precheck")
    public LoginPreCheckDTO authPreCheck(@RequestBody @Valid LoginParam loginParam) {
        final User user = adminService.authenticate(loginParam);
        return new LoginPreCheckDTO(MFAType.useMFA(user.getMfaType()));
    }

    @PostMapping("login")
    @ApiOperation("Login")
    @CacheLock(autoDelete = false, prefix = "login_auth")
    public AuthToken auth(@RequestBody @Valid LoginParam loginParam) {
        return adminService.authCodeCheck(loginParam);
    }

    @PostMapping("logout")
    @ApiOperation("Logs out (Clear session)")
    @CacheLock(autoDelete = false)
    public void logout() {
        adminService.clearToken();
    }

    @PostMapping("password/code")
    @ApiOperation("Sends reset password verify code")
    @CacheLock(autoDelete = false)
    @DisableOnCondition
    public void sendResetCode(@RequestBody @Valid ResetPasswordParam param) {
        adminService.sendResetPasswordCode(param);
    }

    @PutMapping("password/reset")
    @ApiOperation("Resets password by verify code")
    @CacheLock(autoDelete = false)
    @DisableOnCondition
    public void resetPassword(@RequestBody @Valid ResetPasswordParam param) {
        adminService.resetPasswordByCode(param);
    }

    @PostMapping("refresh/{refreshToken}")
    @ApiOperation("Refreshes token")
    @CacheLock(autoDelete = false)
    public AuthToken refresh(@PathVariable("refreshToken") String refreshToken) {
        return adminService.refreshToken(refreshToken);
    }

    @GetMapping("environments")
    @ApiOperation("Gets environments info")
    public EnvironmentDTO getEnvironments() {
        return adminService.getEnvironments();
    }

    @GetMapping(value = "halo/logfile")
    @ApiOperation("Gets halo log file content")
    @DisableOnCondition
    public BaseResponse<String> getLogFiles(@RequestParam("lines") Long lines) {
        return BaseResponse.ok(HttpStatus.OK.getReasonPhrase(), adminService.getLogFiles(lines));
    }
}
