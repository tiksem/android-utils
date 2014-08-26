package com.utilsframework.android.parsers.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: CM
 * Date: 26.12.12
 * Time: 21:07
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedJSONObject{
    JSONObject jsonObject;

    public ExtendedJSONObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public ExtendedJSONObject(String string) throws JSONException{
        this.jsonObject = new JSONObject(string);
    }

    private static JSONObject getHolderFromPath(JSONObject root, String... path) throws JSONException {
        JSONObject resultJsonObject = root;

        for(int i = 0; i < path.length - 1; i++){
            resultJsonObject = resultJsonObject.getJSONObject(path[i]);
        }

        return resultJsonObject;
    }

    private JSONObject getHolderFromPath(String... path) throws JSONException {
        return getHolderFromPath(jsonObject, path);
    }

    public Object getFromPath(String... path) throws JSONException{
        return getHolderFromPath(path).get(path[path.length - 1]);
    }

    public JSONArray getJsonArrayFromPath(String... path) throws JSONException{
        return getHolderFromPath(path).getJSONArray(path[path.length - 1]);
    }

    public static JSONObject getJsonObjectFromPath(JSONObject root, String... path) throws JSONException {
        JSONObject holder = getHolderFromPath(root, path);
        return holder.getJSONObject(path[path.length - 1]);
    }

    public JSONObject getJsonObjectFromPath(String... path) throws JSONException {
        return getJsonObjectFromPath(jsonObject, path);
    }

    public <T> List<T> parseJsonArrayFromPath(JsonArrayElementParser<T> parser, String... path) throws JSONException {
        JSONArray array = getJsonArrayFromPath(path);

        int elementsCount = parser.getElementsCount(jsonObject, array);
        if(elementsCount == -1 || elementsCount > array.length()){
            elementsCount = array.length();
        }

        List<T> result = new ArrayList<T>(elementsCount);

        for(int i = 0; i < elementsCount; i++){
            JSONObject elementJsonObject = array.getJSONObject(i);
            T object = parser.parse(elementJsonObject);
            if(object != null){
                result.add(object);
            }
        }

        return result;
    }

    public String getStringFromPath(String... path) throws JSONException{
        return getHolderFromPath(path).getString(path[path.length - 1]);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
