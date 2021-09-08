package com.rsosor.app.exception;

import org.springframework.http.HttpStatus;

/**
 * BeanUtilsException
 *
 * @author RsosoR
 * @date 2021/9/2
 */
public class BeanUtilsException extends AbstractRsosoRException {

    public BeanUtilsException(String message) {
        super(message);
    }

    public BeanUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
