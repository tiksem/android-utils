package com.utilsframework.android.image;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
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

    public static void takeImageFromCamera(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, requestCode);
        } else {
            throw new UnsupportedOperationException("Camera is not availible");
        }
    }

    public static String getSelectedImagePath(Activity activity, Intent data) {
        File selectedImage = BitmapUtilities.getFromMediaUri(activity.getContentResolver(), data.getData());
        return selectedImage.getAbsolutePath();
    }
}
