package com.dbbest.demos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.dbbest.android.bitmap.BitmapUtilities;
import com.dbbest.android.image.DeliveryResultException;
import com.dbbest.android.image.TakeImageProcessor;
import com.dbbest.android.image.listener.ImageBitmapProcessResult;
import com.dbbest.android.image.listener.ImagePicked;

import java.io.File;

public class MyActivity extends Activity implements ImagePicked {
    private static final String TAG = MyActivity.class.getSimpleName();

    private Button mChooseBtn;
    private ImageView mImageView;

    private TakeImageProcessor mProcessor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mChooseBtn = (Button) findViewById(R.id.choose_btn);
        mImageView = (ImageView) findViewById(R.id.image);
        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File avatarDir = new File(getExternalCacheDir(), "avatar");
                avatarDir.mkdirs();
                final File avatar = new File(avatarDir, "profile.jpg");

                AlertDialog.Builder chooseDialogBuilder = new AlertDialog.Builder(MyActivity.this);
                chooseDialogBuilder.setTitle("Choose image");
                chooseDialogBuilder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent cameraIntent = mProcessor.createTakeFromCameraIntent(Uri.fromFile(avatar));
                        startActivityForResult(cameraIntent, TakeImageProcessor.CAMERA_REQUEST_CODE);
                    }
                });
                chooseDialogBuilder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent galleryIntent = mProcessor.createTakeFromGalleryIntent(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, TakeImageProcessor.GALLERY_REQUEST_CODE);
                    }
                });
                chooseDialogBuilder.create().show();
            }
        });

        mProcessor = new TakeImageProcessor(this);
        mProcessor.setImagePickedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            mProcessor.onHandleActivityResult(requestCode, resultCode, data);
        } catch (DeliveryResultException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImagePicked(int sourceType, Uri pickedImage) {
        switch (sourceType) {
            case TakeImageProcessor.CAMERA_REQUEST_CODE:
            case TakeImageProcessor.GALLERY_REQUEST_CODE:

                Intent cropIntent = mProcessor.createCropImageIntent(pickedImage, pickedImage);
                startActivityForResult(cropIntent, TakeImageProcessor.CROP_REQUEST_CODE);

                mProcessor.setImageProcessResultListener(new ImageBitmapProcessResult() {
                    @Override
                    public void onImageProcessedStart() {
                        Log.d(TAG, "Image crop processing start!");

                        //user 'waiting' component
                    }

                    @Override
                    public void onImageProcessedSuccess(Uri result) {
                        Log.d(TAG, "Image crop finished! " + result);

                        //TODO implement lazy load
                        Bitmap bmpToView = BitmapUtilities.resizeBitmap(MyActivity.this, result, 200, 200);
                        mImageView.setImageBitmap(bmpToView);
                    }

                    @Override
                    public void onImageProcessedWithError(Bitmap source) {
                        Log.d(TAG, "Image crop aborted with errors!");
                        source.recycle();

                        //show errors to user
                    }
                });
                break;
        }
    }
}
