package com.utilsframework.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
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
            Toast.makeText(activity, "Invalid video selected", Toast.LENGTH_SHORT).show();
        }
    }
}
