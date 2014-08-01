package com.utilsframework.android.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.utilsframework.android.ImagePicker;
import com.utilsframework.android.R;
import com.utilsframework.android.view.Alerts;
import com.utilsframework.android.view.OnNo;
import com.utilsframework.android.view.OnYes;

import java.io.File;

public class SelectAndCropImageActivity extends Activity {
    public static final String ASPECT_RATIO_KEY = "ASPECT_RATIO_KEY";
    public static final String SHOULD_SHOW_GALLERY_CAMERA_ALERT = "SHOULD_SHOW_GALLERY_CAMERA_ALERT";
    public static final String GALLERY_CAMERA_ALERT_TEXT = "GALLERY_CAMERA_ALERT_TEXT";

    private Point aspectRatio;
    private boolean shouldShowGalleryCameraAlert;
    private String galleryCameraAlertText;

    public static class Params {
        public Point aspectRatio = null;
        public boolean shouldShowGalleryCameraAlert = false;
        public String galleryCameraAlertText = "Pick image using";
    }

    private static Intent createIntent(Activity activity, Params params) {
        Intent intent = new Intent(activity, SelectAndCropImageActivity.class);
        intent.putExtra(ASPECT_RATIO_KEY, params.aspectRatio);
        if (params.shouldShowGalleryCameraAlert) {
            intent.putExtra(SHOULD_SHOW_GALLERY_CAMERA_ALERT, params.shouldShowGalleryCameraAlert);
            intent.putExtra(GALLERY_CAMERA_ALERT_TEXT, params.galleryCameraAlertText);
        }
        return intent;
    }

    public static void start(Activity activity, Params params, int requestCode) {
        Intent intent = createIntent(activity, params);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, Params params, int requestCode) {
        Intent intent = createIntent(fragment.getActivity(), params);
        fragment.startActivityForResult(intent, requestCode);
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

        shouldShowGalleryCameraAlert = getIntent().getBooleanExtra(SHOULD_SHOW_GALLERY_CAMERA_ALERT, false);
        if(shouldShowGalleryCameraAlert){
            galleryCameraAlertText = getIntent().getStringExtra(GALLERY_CAMERA_ALERT_TEXT);
            if(galleryCameraAlertText == null){
                galleryCameraAlertText = "";
            }
        }

        prepareCrop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_and_crop_image_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void prepareCrop() {
        if (!shouldShowGalleryCameraAlert) {
            ImagePicker.pickImage(this);
        } else {
            Alerts.YesNoAlertSettings settings = new Alerts.YesNoAlertSettings();
            settings.yesButtonText = "Gallery";
            settings.noButtonText = "Camera";
            settings.onYes = new OnYes() {
                @Override
                public void onYes() {
                    ImagePicker.pickImage(SelectAndCropImageActivity.this);
                }
            };
            settings.onNo = new OnNo() {
                @Override
                public void onNo() {
                    ImagePicker.takeImageFromCamera(SelectAndCropImageActivity.this);
                }
            };
            settings.message = galleryCameraAlertText;
            Alerts.showYesNoAlert(this, settings);
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
                beginCrop(result.getData());
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
