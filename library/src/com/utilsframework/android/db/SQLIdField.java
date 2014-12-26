package com.utilsframework.android.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Tikhonenko.S on 21.10.13.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface SQLIdField {
    String type() default "";
}
