package com.utilsframework.android.db;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Tikhonenko.S on 18.09.13.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface SQLDataStoreField {
    String fieldName();
    int maxStringLength() default 0;
}
