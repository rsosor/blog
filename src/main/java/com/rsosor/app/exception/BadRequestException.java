package com.rsosor.app.exception;

import org.springframework.http.HttpStatus;

/**
 * BadRequestException
 *
 * @author RsosoR
 * @date 2021/9/3
 */
public class BadRequestException extends AbstractRsosoRException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
