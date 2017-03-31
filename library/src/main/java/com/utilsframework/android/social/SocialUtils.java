package com.utilsframework.android.social;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.utilsframework.android.AndroidUtilities;
import com.utilsframework.android.system.Intents;
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
            String url = "http://vk.com/id" + userId;
            Intents.openUrl(context, url);
        }
    }

    public static void shareImage(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        context.startActivity(intent);
    }

    public static void sendEmail(Context context, String toEmail, String chooserHeader) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + toEmail));
        context.startActivity(Intent.createChooser(intent, chooserHeader));
    }

    public static void sendEmail(Context context, String toEmail, int chooserHeader) {
        sendEmail(context, toEmail, context.getString(chooserHeader));
    }
}
