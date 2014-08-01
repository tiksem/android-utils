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
import com.utilsframework.android.R;

import java.io.File;

public class SelectAndCropImageActivity extends Activity {
    public static final String ASPECT_RATIO_KEY = "ASPECT_RATIO_KEY";

    private ImageView resultView;
    private Point aspectRatio;

    public static void start(Activity activity, Point aspectRatio, int requestCode) {
        Intent intent = new Intent(activity, SelectAndCropImageActivity.class);
        intent.putExtra(ASPECT_RATIO_KEY, aspectRatio);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void start(Fragment fragment, Point aspectRatio, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), SelectAndCropImageActivity.class);
        intent.putExtra(ASPECT_RATIO_KEY, aspectRatio);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new FrameLayout(this));
        aspectRatio = getIntent().getParcelableExtra(ASPECT_RATIO_KEY);
        if(aspectRatio == null){
            aspectRatio = new Point(0, 0);
        }

        crop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_and_crop_image_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void crop() {
        Crop.pickImage(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_select) {
            crop();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK) {
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
