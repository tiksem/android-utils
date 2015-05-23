package com.utilsframework.android.subscaleview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by semyon.tikhonenko on 23.05.2015.
 */
public class ScaleImagePreviewActivity extends Activity {
    private static final String IMAGE_PATH = "imagePath";
    private static final String IMAGE_URI = "imageURI";

    public static void start(Context context, String imagePath) {
        Intent intent = new Intent(context, ScaleImagePreviewActivity.class);
        intent.putExtra(IMAGE_PATH, imagePath);
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
            String imagePath = getIntent().getParcelableExtra(IMAGE_PATH);
            imageView.setImage(ImageSource.uri("file://" + imagePath));
        }
        setContentView(imageView);
    }
}
