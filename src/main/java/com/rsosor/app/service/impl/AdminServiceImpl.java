package com.rsosor.app.service.impl;

import cn.hutool.core.lang.Validator;
import com.rsosor.app.event.logger.LogEvent;
import com.rsosor.app.exception.BadRequestException;
import com.rsosor.app.exception.NotFoundException;
import com.rsosor.app.model.dto.EnvironmentDTO;
import com.rsosor.app.model.dto.LoginPreCheckDTO;
import com.rsosor.app.model.entity.User;
import com.rsosor.app.model.enums.LogType;
import com.rsosor.app.model.params.LoginParam;
import com.rsosor.app.model.params.ResetPasswordParam;
import com.rsosor.app.security.token.AuthToken;
import com.rsosor.app.service.IAdminService;
import com.rsosor.app.service.IOptionService;
import com.rsosor.app.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * AdminServiceImpl
 *
 * @author RsosoR
 * @date 2021/9/23
 */
@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    private final IOptionService optionService;

    private final IUserService userService;

    private final ApplicationEventPublisher eventPublisher;

    public AdminServiceImpl(
            IOptionService optionService,
            IUserService userService,
            ApplicationEventPublisher eventPublisher) {
        this.optionService = optionService;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @NonNull
    public User authenticate(@NonNull LoginParam loginParam) {
        Assert.notNull(loginParam, "Login param must not be null");

        String username = loginParam.getUsername();

        String mismatchTip = "用戶名或者密碼不正確";

        final User user;

        try {
            // Get user by username or email
            user = Validator.isEmail(username)
                    ? userService.getByEmailOfNonNull(username)
                    : userService.getByUsernameOfNonNull(username);
        } catch (NotFoundException e) {
            log.error("Failed to find user by name: " + username);
            eventPublisher.publishEvent(
                    new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED,
                            loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        userService.mustNotExpire(user);

        if (!userService.passwordMatch(user, loginParam.getPassword())) {
            // If the password is mismatch
            eventPublisher.publishEvent(
                    new LogEvent(this, loginParam.getUsername(), LogType.LOGIN_FAILED,
                            loginParam.getUsername()));

            throw new BadRequestException(mismatchTip);
        }

        return user;
    }

    @Override
    @NonNull
    public AuthToken authCodeCheck(LoginParam loginParam) {
        return null;
    }

    @Override
    public void clearToken() {

    }

    @Override
    public void sendResetPasswordCode(ResetPasswordParam param) {

    }

    @Override
    public void resetPasswordByCode(ResetPasswordParam param) {

    }

    @Override
    @NonNull
    public EnvironmentDTO getEnvironments() {
        return null;
    }

    @Override
    public AuthToken refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public String getLogFiles(Long lines) {
        return null;
    }

    @Override
    public LoginPreCheckDTO getUserEnv(String username) {
        return null;
    }
}
