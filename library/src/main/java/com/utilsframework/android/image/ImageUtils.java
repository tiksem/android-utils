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
    public static final int PICK_IMAGE_FROM_GALLERY_REQUEST_CODE = 33455;

    public static void pickImageFromGallery(Activity activity, int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, requestCode);
    }

    public static void pickImageFromGallery(Activity activity) {
        pickImageFromGallery(activity, PICK_IMAGE_FROM_GALLERY_REQUEST_CODE);
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
        if (selectedImage == null) {
            return null;
        }

        return selectedImage.getAbsolutePath();
    }

    public static String handleImagePick(Activity activity,
                                         int requestCode,
                                         int resultCode,
                                         Intent data) {
        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST_CODE &&
                resultCode == Activity.RESULT_OK) {
            return getSelectedImagePath(activity, data);
        }

        return null;
    }
}
