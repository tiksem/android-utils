package com.utilsframework.android.image;

import android.app.Activity;
import android.content.Intent;
import com.utilsframework.android.bitmap.BitmapUtilities;

import java.io.File;

/**
 * Created by semyon.tikhonenko on 22.05.2015.
 */
public class ImageUtils {
    public static void pickImageFromGallery(Activity activity, int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, requestCode);
    }

    public static String getSelectedImagePath(Activity activity, Intent data) {
        File selectedImage = BitmapUtilities.getFromMediaUri(activity.getContentResolver(), data.getData());
        return selectedImage.getAbsolutePath();
    }
}
