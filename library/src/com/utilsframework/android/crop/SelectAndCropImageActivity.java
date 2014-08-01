package com.utilsframework.android.crop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.utilsframework.android.ImagePicker;
import com.utilsframework.android.R;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.GuiUtilities;
import com.utilsframework.android.view.OnNo;
import com.utilsframework.android.view.OnYes;

import java.io.File;

public class SelectAndCropImageActivity extends Activity {
    public static final String ASPECT_RATIO_KEY = "ASPECT_RATIO_KEY";
    public static final String SHOULD_PICK_IMAGE_FROM_CAMERA = "SHOULD_PICK_IMAGE_FROM_CAMERA";

    private Point aspectRatio;
    private boolean shouldTakeImageFromCamera = false;
    private String cameraPath;

    public static class Params {
        public Point aspectRatio = null;
        public boolean shouldShowGalleryCameraAlert = false;
        public String galleryCameraAlertText = "Pick image using";
    }

    private static void start(final Activity activity, final Fragment fragment,
                              Params params, final int requestCode) {
        Context context = activity != null ? activity : fragment.getActivity();

        final Intent intent = new Intent(context, SelectAndCropImageActivity.class);
        intent.putExtra(ASPECT_RATIO_KEY, params.aspectRatio);

        if(params.shouldShowGalleryCameraAlert){
            String galleryCameraAlertText = params.galleryCameraAlertText;
            if(galleryCameraAlertText == null){
                galleryCameraAlertText = "";
            }

            Alerts.YesNoAlertSettings settings = new Alerts.YesNoAlertSettings();
            settings.yesButtonText = "Gallery";
            settings.noButtonText = "Camera";
            settings.onYes = new OnYes() {
                @Override
                public void onYes() {
                    GuiUtilities.startActivityForResult(activity, fragment, intent, requestCode);
                }
            };
            settings.onNo = new OnNo() {
                @Override
                public void onNo() {
                    intent.putExtra(SHOULD_PICK_IMAGE_FROM_CAMERA, true);
                    GuiUtilities.startActivityForResult(activity, fragment, intent, requestCode);
                }
            };
            settings.title = galleryCameraAlertText;
            Alerts.showYesNoAlert(context, settings);
        } else {
            GuiUtilities.startActivityForResult(activity, fragment, intent, requestCode);
        }
    }

    public static void start(Activity activity, Params params, int requestCode) {
        start(activity, null, params, requestCode);
    }

    public static void start(Fragment fragment, Params params, int requestCode) {
        start(null, fragment, params, requestCode);
    }

    public static void start(Fragment fragment, Point aspectRatio, int requestCode) {
        Params params = new Params();
        params.aspectRatio = aspectRatio;
        start(fragment, params, requestCode);
    }

    public static void start(Activity activity, Point aspectRatio, int requestCode) {
        Params params = new Params();
        params.aspectRatio = aspectRatio;
        start(activity, params, requestCode);
    }

    public static void start(Fragment fragment, int requestCode) {
        Params params = new Params();
        start(fragment, params, requestCode);
    }

    public static void start(Activity activity, int requestCode) {
        Params params = new Params();
        start(activity, params, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FrameLayout(this));
        aspectRatio = getIntent().getParcelableExtra(ASPECT_RATIO_KEY);
        if(aspectRatio == null){
            aspectRatio = new Point(0, 0);
        }

        shouldTakeImageFromCamera = getIntent().getBooleanExtra(SHOULD_PICK_IMAGE_FROM_CAMERA, false);
        prepareCrop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_and_crop_image_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private String generateCameraPhotoPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/camera" + System.currentTimeMillis() + ".jpg";
    }

    private void prepareCrop() {
        if (!shouldTakeImageFromCamera) {
            ImagePicker.pickImage(this);
        } else {
            cameraPath = generateCameraPhotoPath();
            ImagePicker.takeImageFromCamera(this, cameraPath, true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_select) {
            prepareCrop();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == ImagePicker.REQUEST_PICK || requestCode == ImagePicker.REQUEST_CAMERA_PICK) {
            if (resultCode == RESULT_OK) {
                if (requestCode == ImagePicker.REQUEST_PICK) {
                    beginCrop(result.getData());
                } else {
                    beginCrop(Uri.fromFile(new File(cameraPath)));
                }
            } else {
                finish();
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(source).output(outputUri).withAspect(aspectRatio.x, aspectRatio.y).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.setData(Crop.getOutput(result));
            setResult(resultCode, intent);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
            setResult(resultCode);
        }
        finish();
    }

}
