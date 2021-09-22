package com.rsosor.app.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception to entity not found.
 *
 * @author RsosoR
 * @date 2021/9/2
 */
public class NotFoundException extends AbstractRsosoRException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
