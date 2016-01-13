package com.utilsframework.android;

import android.content.Context;
import com.utils.framework.io.IOUtilities;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by stykhonenko on 18.11.15.
 */
public class AndroidIOUtils {
    public static String readStringFromAsset(Context context, String assetFileName) throws IOException {
        InputStream inputStream = context.getAssets().open(assetFileName);
        return IOUtilities.toString(inputStream);
    }
}
