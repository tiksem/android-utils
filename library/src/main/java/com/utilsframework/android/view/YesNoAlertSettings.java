package com.utilsframework.android.view;

import android.content.Context;

import com.utilsframework.android.R;

public class YesNoAlertSettings {
    public String title;
    public String message;
    public int yesButtonText = R.string.yes;
    public int noButtonText = R.string.no;
    public Context context;

    public void setMessage(int messageId) {
        message = context.getString(messageId);
    }

    public void setMessage(int messageId, Object... args) {
        message = context.getString(messageId, args);
    }

    public void setTitle(int titleId) {
        title = context.getString(titleId);
    }

    public void setTitle(int titleId, Object... args) {
        title = context.getString(titleId, args);
    }

    public YesNoAlertSettings(Context context) {
        this.context = context;
    }

    public void onYes() {}
    public void onNo() {}
}
