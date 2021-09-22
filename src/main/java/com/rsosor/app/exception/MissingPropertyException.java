package com.rsosor.app.exception;

/**
 * Missing property value exception.
 *
 * @author RsosoR
 * @date 2021/9/3
 */
public class MissingPropertyException extends BadRequestException {

    public MissingPropertyException(String message) {
        super(message);
    }

    public MissingPropertyException(String message, Throwable cause) {
        super(message, cause);
    }
}
