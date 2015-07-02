package com.utilsframework.android;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tikhonenko.S on 30.10.13.
 */
public final class ExecuteTimeLogger {
    private static Map<String,Long> times = new HashMap<String, Long>();

    public static void timeStart(String name){
        long currentTime = System.currentTimeMillis();

        times.put(name, currentTime);
    }

    public static void timeEnd(String name, String tag){
        long currentTime = System.currentTimeMillis();

        if(!times.containsKey(name)){
            return;
        }

        long startTime = times.get(name);
        long timeDif = currentTime - startTime;
        times.remove(name);
        Log.i(tag, name + " " + timeDif);
    }

    public static void timeEnd(String name) {
        timeEnd(name, ExecuteTimeLogger.class.getSimpleName());
    }
}
