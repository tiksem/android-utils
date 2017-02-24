package com.utilsframework.android.network;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogRequestListener<Result, ErrorType> extends RequestListener<Result, ErrorType> {
    private Context context;
    private String message;
    private ProgressDialog progressDialog;

    public ProgressDialogRequestListener(Context context, int messageResourceId, Object... args) {
        this.context = context;
        message = context.getString(messageResourceId, args);
    }

    @Override
    public void onPreExecute() {
        progressDialog = ProgressDialog.show(context, null, message);
    }

    @Override
    public void onAfterCompleteOrCanceled() {
        progressDialog.dismiss();
    }
}
