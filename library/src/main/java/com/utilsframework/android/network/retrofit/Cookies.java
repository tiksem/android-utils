package com.utilsframework.android.network.retrofit;

import com.utils.framework.CollectionUtils;
import com.utils.framework.Predicate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Cookie;

public class Cookies {
    public static void removeExpiredCookies(List<Cookie> cookies) {
        CollectionUtils.removeAll(cookies, new Predicate<Cookie>() {
            @Override
            public boolean check(Cookie item) {
                return item.expiresAt() < System.currentTimeMillis();
            }
        });
    }
}
