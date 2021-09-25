package com.rsosor.app.security.authentication;

import com.rsosor.app.security.support.UserDetail;

/**
 * AuthenticationImpl
 *
 * @author RsosoR
 * @date 2021/9/25
 */
public class AuthenticationImpl implements Authentication {

    private final UserDetail userDetail;

    public AuthenticationImpl(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public UserDetail getDetail() {
        return userDetail;
    }
}
