package com.utilsframework.android.parsers.json;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 2/14/13
 * Time: 8:01 PM
 */
public interface JsonParser<T> {
    T parse(JSONObject jsonObject) throws JSONException;
}
