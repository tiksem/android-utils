package com.utilsframework.android.subscaleview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.File;

/**
 * Created by semyon.tikhonenko on 23.05.2015.
 */
public class ScaleImagePreviewActivity extends Activity {
    private static final String IMAGE_PATH = "imagePath";
    private static final String IMAGE_URI = "imageURI";
    private static final String DELETE_FILE = "deleteFile";
    private String imagePath;

    public static void start(Context context, String imagePath) {
        start(context, imagePath, false);
    }

    public static void start(Context context, String imagePath, boolean deleteFile) {
        Intent intent = new Intent(context, ScaleImagePreviewActivity.class);
        intent.putExtra(IMAGE_PATH, imagePath);
        if (deleteFile) {
            intent.putExtra(DELETE_FILE, true);
        }

        context.startActivity(intent);
    }

    public static void start(Context context, Uri imagePath) {
        Intent intent = new Intent(context, ScaleImagePreviewActivity.class);
        intent.putExtra(IMAGE_URI, imagePath);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(this);
        Uri imageUri = getIntent().getParcelableExtra(IMAGE_URI);
        if (imageUri != null) {
            imageView.setImage(ImageSource.uri(imageUri));
        } else {
            imagePath = getIntent().getStringExtra(IMAGE_PATH);
            imageView.setImage(ImageSource.uri("file://" + imagePath));
        }
        setContentView(imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getIntent().getBooleanExtra(DELETE_FILE, false)) {
            new File(imagePath).delete();
        }
    }
}
