package com.rsosor.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Base exception to the project.
 *
 * @author RsosoR
 * @date 2021/9/2
 */
public abstract class AbstractRsosoRException extends RuntimeException {

    /**
     * Error errorData.
     */
    private Object errorData;

    public AbstractRsosoRException(String message) {
        super(message);
    }

    public AbstractRsosoRException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Http status code
     *
     * @return {@link HttpStatus}
     */
    @NonNull
    public abstract HttpStatus getStatus();

    @Nullable
    public Object getErrorData() {
        return errorData;
    }

    /**
     * Sets error errorData.
     *
     * @param errorData error data
     * @return current exception.
     */
    @NonNull
    public AbstractRsosoRException setErrorData(@Nullable Object errorData) {
        this.errorData = errorData;
        return this;
    }
}
