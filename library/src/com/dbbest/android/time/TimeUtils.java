package com.dbbest.android.time;

import android.util.Log;

/**
 * User: Tikhonenko.S
 * Date: 03.03.14
 * Time: 16:25
 */
public class TimeUtils {
    private static long startTimeInMilliseconds = System.currentTimeMillis();
    private static long startNanoSeconds = System.nanoTime();

    public static long getTimeInMicroSeconds(){
        long milliSeconds = System.currentTimeMillis() - startTimeInMilliseconds;
        long nanoSeconds = System.nanoTime() - startNanoSeconds;

        Log.i("yoyoyoy", "nano = " + nanoSeconds);
        Log.i("yoyoyoy", "mili = " + milliSeconds);

        return nanoSeconds / 1000 + milliSeconds * 1000;
    }
}
