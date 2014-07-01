package com.utilsframework.android.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import com.utilsframework.android.R;

/**
 * User: Tikhonenko.S
 * Date: 01.07.14
 * Time: 18:30
 */
public class ImagePreviewActivity extends FragmentActivity {
    private static final String BITMAP_KEY = "BITMAP_KEY";

    public static void show(Context context, Bitmap bitmap){
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(BITMAP_KEY, bitmap);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);

        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageBitmap((Bitmap) getIntent().getParcelableExtra(BITMAP_KEY));
    }
}
