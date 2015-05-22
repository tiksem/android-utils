package com.utilsframework.android.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import com.utilsframework.android.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * User: Tikhonenko.S
 * Date: 01.07.14
 * Time: 18:30
 */
public class ImagePreviewActivity extends Activity {
    private static final String BITMAP_KEY = "BITMAP_KEY";

    public static void show(Context context, Bitmap bitmap){
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        File file = new File(context.getCacheDir(), "ImagePreviewActivity.png");
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        intent.putExtra(BITMAP_KEY, file.getAbsolutePath());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_preview);
        String path = getIntent().getStringExtra(BITMAP_KEY);

        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }
}
