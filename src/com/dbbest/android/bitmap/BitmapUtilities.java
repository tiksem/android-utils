package com.dbbest.android.bitmap;

import android.graphics.Bitmap;

/**
 * User: mda
 * Date: 10/3/13
 * Time: 3:02 PM
 */
public class BitmapUtilities {
    public static boolean bitmapCompare(Bitmap bmp1, Bitmap bmp2) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bmp1.sameAs(bmp2);
        }

        return sameAs(bmp1, bmp2);
    }

    private static boolean sameAs(Bitmap bmp1, Bitmap bmp2) {
        if(bmp1 == null || bmp2 == null) {
            return true;
        }
        if((bmp1.getHeight() != bmp2.getHeight()) || (bmp1.getWidth() != bmp2.getWidth())) {
            return false;
        }
        int width = bmp1.getWidth();
        int height = bmp1.getHeight();
        for(int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {
                if(bmp1.getPixel(j, i) != bmp2.getPixel(j, i)) {
                    return false;
                }
            }
        }

        return true;
    }
}
