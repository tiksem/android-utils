package com.utilsframework.android.parsers.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Project: FlyingDog
 * User: stikhonenko
 * Date: 2/14/13
 * Time: 8:02 PM
 */
public abstract class JsonArrayElementParser<T> implements JsonParser<T>{
    protected int getElementsCount(JSONObject root, JSONArray array) throws JSONException{
        return -1;
    }
}
