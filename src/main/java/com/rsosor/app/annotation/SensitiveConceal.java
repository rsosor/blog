package com.rsosor.app.annotation;

import java.lang.annotation.*;

/**
 * SensitiveConceal
 *
 * @author RsosoR
 * @date 2021/9/11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SensitiveConceal {
}
