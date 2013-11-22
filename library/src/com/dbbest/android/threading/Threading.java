package com.dbbest.android.threading;

import android.os.AsyncTask;

/**
 * User: Tikhonenko.S
 * Date: 22.11.13
 * Time: 17:26
 */
public final class Threading {
    public static void runOnBackground(final ThrowingRunnable action, final OnFinish<Throwable> onFinish){
        new AsyncTask<Void, Void, Throwable>(){
            @Override
            protected Throwable doInBackground(Void... params) {
                try {
                    action.run();
                } catch (Throwable e) {
                    return e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Throwable throwable) {
                if(onFinish != null){
                    onFinish.onFinish(throwable);
                } else if(throwable != null) {
                    throw new RuntimeException(throwable);
                }
            }
        }.execute();
    }

    public static void runOnBackground(final ThrowingRunnable action){
        runOnBackground(action, null);
    }
}
