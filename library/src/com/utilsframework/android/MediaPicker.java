package com.utilsframework.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * User: Tikhonenko.S
 * Date: 08.08.14
 * Time: 19:22
 */
public final class MediaPicker {
    // onActivityResult use Intent result.getData() to retrieve video path
    public static void pickVideo(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, "Unsupported", Toast.LENGTH_SHORT).show();
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getVideoPathFromPickVideo(Context context, Intent result) {
        return getRealPathFromURI(context, result.getData());
    }
}
