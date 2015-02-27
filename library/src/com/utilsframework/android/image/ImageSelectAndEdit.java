package com.utilsframework.android.image;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import com.utilsframework.android.bitmap.BitmapUtilities;
import com.utilsframework.android.file.FileUtils;
import com.utilsframework.android.file.IoUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by CM on 2/27/2015.
 */
public class ImageSelectAndEdit {
    public static final int PICK = 12321;
    public static final int EDIT = 12322;
    private Activity activity;
    private File selectedImage;
    private File outputFile;

    public ImageSelectAndEdit(Activity activity) {
        this.activity = activity;
    }

    public void selectAndEdit() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        activity.startActivityForResult(photoPickerIntent, PICK);
    }

    public interface Listener {
        public void onOk(File file);
    }

    private void onNotOk() {
        selectedImage = null;
        outputFile = null;
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data, Listener listener) {
        if (requestCode == PICK) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    selectedImage = BitmapUtilities.getFromMediaUri(activity.getContentResolver(), data.getData());
                    String selectedImagePath = selectedImage.getAbsolutePath();
                    outputFile = new File(Environment.getExternalStorageDirectory(), "temp." +
                    FileUtils.getExtension(selectedImagePath));
                    IoUtils.copyFile(selectedImagePath, outputFile.getAbsolutePath());

                    Intent editIntent = new Intent(Intent.ACTION_EDIT);
                    editIntent.setDataAndType(BitmapUtilities.getImageContentUri(activity, outputFile), "image/*");
                    editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivityForResult(Intent.createChooser(editIntent, null), EDIT);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                onNotOk();
            }

            return true;
        } else if(requestCode == EDIT) {
            if (resultCode == Activity.RESULT_OK) {
                listener.onOk(outputFile);
            } else {
                onNotOk();
            }

            return true;
        }

        return false;
    }

    public void deleteFile() {
        outputFile.delete();
    }
}
