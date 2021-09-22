package com.rsosor.app.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.rsosor.app.exception.ForbiddenException;
import com.rsosor.app.exception.NotFoundException;
import com.rsosor.app.model.entity.User;
import com.rsosor.app.repository.IUserRepository;
import com.rsosor.app.service.IUserService;
import com.rsosor.app.service.base.AbstractCrudService;
import com.rsosor.app.utils.DateUtils;
import com.rsosor.app.utils.RsosoRUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * UserServiceImpl
 *
 * @author RsosoR
 * @date 2021/9/23
 */
@Service
public class UserServiceImpl extends AbstractCrudService<User, Integer> implements IUserService {

    private final IUserRepository userRepository;

    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(IUserRepository userRepository,
                           ApplicationEventPublisher eventPublisher) {
        super(userRepository);
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByUsernameOfNonNull(String username) {
        return getByUsername(username).orElseThrow(
                () -> new NotFoundException("The username does not exist").setErrorData(username));
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getByEmailOfNonNull(String email) {
        return getByEmail(email).orElseThrow(
                () -> new NotFoundException("The email does not exist").setErrorData(email));
    }

    @Override
    public void mustNotExpire(User user) {
        Assert.notNull(user, "User must not be null");

        Date now = DateUtils.now();
        if (user.getExpireTime() != null && user.getExpireTime().after(now)) {
            long seconds =
                    TimeUnit.MILLISECONDS.toSeconds(user.getExpireTime().getTime() - now.getTime());
            // If expired
            throw new ForbiddenException("帳號已被停用，請 " + RsosoRUtils.timeFormat(seconds) + " 後重試")
                    .setErrorData(seconds);
        }
    }

    @Override
    public boolean passwordMatch(User user, String plainPassword) {
        Assert.notNull(user, "User must not be null");

        return !StringUtils.isBlank(plainPassword)
                && BCrypt.checkpw(plainPassword, user.getPassword());
    }
}
