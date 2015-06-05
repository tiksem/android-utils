package com.utilsframework.android.social;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by semyon.tikhonenko on 02.06.2015.
 */
public class SocialUtils {
    public static void postOnInstagram(Context context, Uri uri) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/*");

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setPackage("com.instagram.android");
        context.startActivity(shareIntent);
    }

    public static void postOnInstagram(Context context, String filePath) {
        postOnInstagram(context, Uri.fromFile(new File(filePath)));
    }
}
