package com.rsosor.app.annotation;

import com.rsosor.app.model.enums.Mode;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 該註解可以限制某些條件下禁止訪問 api
 *
 * @author RsosoR
 * @date 2021/9/3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisableOnCondition {
    @AliasFor("mode")
    Mode value() default Mode.DEMO;

    @AliasFor("value")
    Mode mode() default Mode.DEMO;
}
