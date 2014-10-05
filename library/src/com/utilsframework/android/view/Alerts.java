package com.utilsframework.android.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import com.utilsframework.android.threading.AsyncOperationCallback;
import com.utilsframework.android.threading.Cancelable;
import com.utilsframework.android.threading.Threading;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 17:21
 */
public final class Alerts {
    public static class YesNoAlertSettings {
        public CharSequence title = "";
        public CharSequence message = null;
        public CharSequence yesButtonText = "Yes";
        public CharSequence noButtonText = "No";
        public OnYes onYes;
        public OnNo onNo;
    }

    public static void showYesNoAlert(Context context, final YesNoAlertSettings settings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton(settings.yesButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(settings.onYes != null){
                    settings.onYes.onYes();
                }
            }
        });

        builder.setNegativeButton(settings.noButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(settings.onNo != null){
                    settings.onNo.onNo();
                }
            }
        });

        builder.setTitle(settings.title);
        if (settings.message != null) {
            builder.setMessage(settings.message);
        }

        builder.create().show();
    }

    public static class OkAlertSettings {
        public CharSequence title = "";
        public CharSequence message = "";
        public CharSequence okButtonText = "OK";
        public OnOk onOk;
    }

    public static void showOkButtonAlert(Context context, final OkAlertSettings settings) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton(settings.okButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(settings.onOk != null){
                    settings.onOk.onOk();
                }
            }
        });

        builder.setTitle(settings.title);
        builder.setMessage(settings.message);

        builder.create().show();
    }

    public static void showOkButtonAlert(Context context, String message) {
        OkAlertSettings okAlertSettings = new OkAlertSettings();
        okAlertSettings.message = message;
        showOkButtonAlert(context, okAlertSettings);
    }

    public static AlertDialog showAlertWithCustomView(Context context, View view, CharSequence message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(view);
        builder.setMessage(message);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    public static ProgressDialog showCircleProgressDialog(Context context, CharSequence message) {
        return ProgressDialog.show(context, null, message);
    }

    public static <T> Cancelable runAsyncOperationWithCircleLoading(Context context,
                                                                CharSequence message,
                                                                final AsyncOperationCallback<T> callback) {
        final ProgressDialog progressDialog = showCircleProgressDialog(context, message);

        new AsyncTask<Void, Void, T>(){
            @Override
            protected T doInBackground(Void... params) {
                return callback.runOnBackground();
            }

            @Override
            protected void onPostExecute(T result) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    callback.onFinish(result);
                }
            }
        }.execute();

        // TODO improve cancelable
        return new Cancelable() {
            @Override
            public void cancel() {
                progressDialog.dismiss();
            }
        };
    }

    public static <T> Cancelable runAsyncOperationWithCircleLoading(Context context,
                                                                    int resourceId,
                                                                    final AsyncOperationCallback<T> callback) {
        return runAsyncOperationWithCircleLoading(context, context.getString(resourceId), callback);
    }

    public static <T> Cancelable runAsyncOperationWithCircleLoading(Context context,
                                                                    CharSequence message,
                                                                    final Runnable operation) {
        return runAsyncOperationWithCircleLoading(context, message, new AsyncOperationCallback<Object>() {
            @Override
            public Object runOnBackground() {
                operation.run();
                return null;
            }

            @Override
            public void onFinish(Object result) {

            }
        });
    }

    public static <T> Cancelable runAsyncOperationWithCircleLoading(Context context,
                                                                    int resourceId,
                                                                    final Runnable operation) {
        return runAsyncOperationWithCircleLoading(context, context.getString(resourceId), operation);
    }
}
