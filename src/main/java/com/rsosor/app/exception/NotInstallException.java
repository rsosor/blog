package com.rsosor.app.exception;

/**
 * NotInstallException
 *
 * @author RsosoR
 * @date 2021/9/26
 */
public class NotInstallException extends BadRequestException {

    public NotInstallException(String message) {
        super(message);
    }

    public NotInstallException(String message, Throwable cause) {
        super(message, cause);
    }
}
