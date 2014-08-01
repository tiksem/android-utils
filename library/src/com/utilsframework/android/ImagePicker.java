package com.utilsframework.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * User: Tikhonenko.S
 * Date: 01.08.14
 * Time: 16:44
 */
public final class ImagePicker {
    public static final int REQUEST_PICK = 9162;
    public static final int REQUEST_CAMERA_PICK = REQUEST_PICK + 1;

    /**
     * Utility method that starts an image picker since that often precedes a crop
     *
     * @param activity Activity that will receive result
     */
    public static void pickImage(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        try {
            activity.startActivityForResult(intent, REQUEST_PICK);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.crop__pick_error, Toast.LENGTH_SHORT).show();
        }
    }

    public static void takeImageFromCamera(Activity activity) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(cameraIntent, REQUEST_CAMERA_PICK);
    }
}
