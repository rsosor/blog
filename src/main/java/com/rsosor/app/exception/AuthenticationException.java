package com.rsosor.app.exception;

import org.springframework.http.HttpStatus;

/**
 * AuthenticationException
 *
 * @author RsosoR
 * @date 2021/9/25
 */
public class AuthenticationException extends AbstractRsosoRException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
