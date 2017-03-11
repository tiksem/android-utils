package com.utilsframework.android.system;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Intents {
    public static void openUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}
