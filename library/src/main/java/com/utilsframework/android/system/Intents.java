package com.utilsframework.android.system;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

public class Intents {
    public static void openUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static Intent intentWithIntExtra(String key, int value) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        return intent;
    }

    public static Intent intentWithParcelableExtra(String key, Parcelable value) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        return intent;
    }
}
