package com.rsosor.app.utils;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import javax.validation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Object validation utilities.
 *
 * @author RsosoR
 * @date 2021/9/9
 */
public class ValidationUtils {

    private static volatile Validator VALIDATOR;

    private ValidationUtils() {
    }

    /**
     * Gets validator, or create it.
     *
     * @return validator
     */
    @NonNull
    public static Validator getValidator() {
        if (VALIDATOR == null) {
            synchronized (ValidationUtils.class) {
                if (VALIDATOR == null) {
                    // Init the validation
                    VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
                }
            }
        }

        return VALIDATOR;
    }

    /**
     * Validates bean by hand.
     *
     * @param obj bean to be validated
     * @param groups validation group
     * @throws ConstraintViolationException throw if validation failure
     */
    public static void validate (Object obj, Class<?>... groups) {

        Validator validator = getValidator();

        if (obj instanceof Iterable) {
            // validate for iterable
            validate((Iterable<?>) obj, groups);
        } else {
            // validate the non-iterable object
            Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj, groups);

            if (!CollectionUtils.isEmpty(constraintViolations)) {
                // If contain some errors then throw constraint violation exception
                throw new ConstraintViolationException(constraintViolations);
            }
        }
    }

    /**
     * Validates iterable objects.
     *
     * @param objs iterable objects could be null
     * @param groups validation groups
     */
    public static void validate(@Nullable Iterable<?> objs, @Nullable Class<?>... groups) {
        if (objs == null) {
            return;
        }

        // get validator
        Validator validator = getValidator();

        // wrap index
        AtomicInteger i = new AtomicInteger(0);
        final Set<ConstraintViolation<?>> allViolations = new LinkedHashSet<>();
        objs.forEach(obj -> {
            int index = i.getAndIncrement();
            Set<? extends ConstraintViolation<?>> violations = violations = validator.validate(obj, groups);
            violations.forEach(violation -> {
                Path path = violation.getPropertyPath();
                if (path instanceof PathImpl) {
                    PathImpl pathImpl = (PathImpl) path;
                    pathImpl.makeLeafNodeIterableAndSetIndex(index);
                }
                allViolations.add(violation);
            });
        });
        if (!CollectionUtils.isEmpty(allViolations)) {
            throw new ConstraintViolationException(allViolations);
        }
    }

    /**
     * 將字段驗證錯誤轉換為標準的 map 型，key:value = field:message
     *
     * @param constraintViolations constraint violations(contain error information)
     * @return error detail map
     */
    @NonNull
    public static Map<String, String> mapWithValidError(
            Set<ConstraintViolation<?>> constraintViolations) {
        if (CollectionUtils.isEmpty(constraintViolations)) {
            return Collections.emptyMap();
        }

        Map<String, String> errMap = new HashMap<>(4);
        // Format the error message
        constraintViolations.forEach(constraintViolation ->
                errMap.put(constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage()));
        return errMap;
    }

    /**
     *
     */
}
