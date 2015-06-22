package com.utilsframework.android.network;

import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by CM on 6/16/2015.
 */
public interface RequestExecutor {
    public String executeRequest(String url, Map<String, Object> args) throws IOException;
}
