package com.utilsframework.android.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * User: Tikhonenko.S
 * Date: 04.04.14
 * Time: 17:21
 */
public final class Alerts {
    public static class YesNoAlertSettings {
        public CharSequence title = "";
        public CharSequence message = "";
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
        builder.setMessage(settings.message);

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
}
