package com.utilsframework.android.social;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.web.WebViewActivity;

import java.io.File;

/**
 * Created by semyon.tikhonenko on 02.06.2015.
 */
public class SocialUtils {
    public static final String VK_PACKAGE_NAME = "com.vkontakte.android";
    public static final String INSTGRAM_PACKAGE_NAME = "com.instagram.android";

    public static void postOnInstagram(Context context, Uri uri) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/*");

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setPackage(INSTGRAM_PACKAGE_NAME);
        context.startActivity(shareIntent);
    }

    public static void postOnInstagram(Context context, String filePath) {
        postOnInstagram(context, Uri.fromFile(new File(filePath)));
    }

    public static boolean isVkInstalled(Context context) {
        return AndroidUtilities.isAppInstalled(context, VK_PACKAGE_NAME);
    }

    public static void startVkUserProfileActivity(Context context, long userId) {
        Uri uri = Uri.parse("vkontakte://profile/" + userId);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void openVkUserProfile(Context context, long userId) {
        if (isVkInstalled(context)) {
            startVkUserProfileActivity(context, userId);
        } else {
            WebViewActivity.loadUrl(context, "http://vk.com/id" + userId);
        }
    }
}
