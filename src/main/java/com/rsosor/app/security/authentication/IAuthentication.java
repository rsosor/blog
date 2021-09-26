package com.rsosor.app.security.authentication;

import com.rsosor.app.security.support.UserDetail;
import org.springframework.lang.NonNull;

/**
 * Authentication
 *
 * @author RsosoR
 * @date 2021/9/25
 */
public interface IAuthentication {

    /**
     * Get user detail.
     *
     * @return user detail
     */
    @NonNull
    UserDetail getDetail();
}
