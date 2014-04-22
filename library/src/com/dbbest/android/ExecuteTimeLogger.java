package com.dbbest.android;

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

        if(times.containsKey(name)){
            throw new IllegalArgumentException("timeStart + " + name + "  exists");
        }

        times.put(name, currentTime);
    }

    public static void timeEnd(String name){
        long currentTime = System.currentTimeMillis();

        if(!times.containsKey(name)){
            throw new IllegalArgumentException("timeEnd + " + name + "does not exist");
        }

        long startTime = times.get(name);
        long timeDif = currentTime - startTime;
        times.remove(name);
        Log.i("ExecuteTimeLogger", name + " " + timeDif);
    }
}
