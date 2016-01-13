package com.utilsframework.android.bitmap;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.*;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.utilsframework.android.threading.OnFinish;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * User: mda
 * Date: 10/3/13
 * Time: 3:02 PM
 */
public class BitmapUtilities {
    private static final int DOMINANT_CALCULATION_COLOR_STEP = 10;

    private static final String SCHEME_FILE = "file";
    private static final String SCHEME_CONTENT = "content";

    @SuppressLint("NewApi")
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

    public static Bitmap rotateBitmapUsingExif(Uri path, Bitmap bitmap) {
        Matrix matrix = new Matrix();

        int rotation = getExifRotation(new File(path.getPath()));
        matrix.preRotate(rotation);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap resizeBitmap(Context ctx, int resourceId, int reqWidth, int reqHeight) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(ctx.getResources(), resourceId, opt);

        final int width = opt.outWidth;
        final int height = opt.outHeight;

        if (width > reqWidth || height > reqHeight) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            opt.inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        opt.inJustDecodeBounds = false;
        opt.inPurgeable = true;

        return BitmapFactory.decodeResource(ctx.getResources(), resourceId, opt);
    }

    public static Bitmap resizeBitmap(Context ctx, Uri imageUri, int reqWidth, int reqHeight) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageUri.getPath(), opt);

        final int width = opt.outWidth;
        final int height = opt.outHeight;

        if (width > reqWidth || height > reqHeight) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            opt.inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }

        opt.inJustDecodeBounds = false;
        opt.inPurgeable = true;

        return BitmapFactory.decodeFile(imageUri.getPath(), opt);
    }

    public static Bitmap resizeExistingBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float currentRatio = ((float) width) / height;
        float scaleWidth = 1;
        float scaleHeight = 1;

        if(newHeight > newWidth)
        {
            scaleWidth = ((float) newWidth) / width;
            scaleHeight = scaleWidth;
        }
        else
        {
            scaleHeight = (((float)newHeight) / height);
            scaleWidth = scaleHeight;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    public static Bitmap scaleExistingBitmap(Bitmap bm, float scaleWidth, float scaleHeight)
    {

        int width = bm.getWidth();
        int height = bm.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }



    public static Bitmap decodeFile(Uri imageUri, boolean quickAndDirty, DisplayMetrics displayMetrics) {
        return decodeFile(new File(imageUri.getPath()), quickAndDirty, displayMetrics);
    }

    // decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File bitmapFile, boolean quickAndDirty, DisplayMetrics displayMetrics) {
        if (bitmapFile.exists()) {
            try {

                // Decode image size
                BitmapFactory.Options bitmapSizeOptions = new BitmapFactory.Options();
                bitmapSizeOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), bitmapSizeOptions);

                // load image using inSampleSize adapted to required image size
                BitmapFactory.Options bitmapDecodeOptions = new BitmapFactory.Options();
                bitmapDecodeOptions.inTempStorage = new byte[16 * 1024];
                bitmapDecodeOptions.inSampleSize = computeInSampleSize(bitmapSizeOptions,
                        (int) (displayMetrics.widthPixels * 0.8), (int) (displayMetrics.heightPixels * 0.8), false);
                Log.i("BitmapDecodeOptions", "bitmapDecodeOptions.inSampleSize = " + bitmapDecodeOptions.inSampleSize);
                bitmapDecodeOptions.inPurgeable = true;
                bitmapDecodeOptions.inDither = !quickAndDirty;
                bitmapDecodeOptions.inPreferredConfig = quickAndDirty ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888;
                Bitmap decodedBitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), bitmapDecodeOptions);

                Bitmap resultBitmap = getRotatedBitmap(bitmapFile.getAbsolutePath(),decodedBitmap);
                return resultBitmap;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * compute powerOf2 or exact scale to be used as {@link BitmapFactory.Options#inSampleSize} value (for subSampling)
     *
     * @param dstWidth
     * @param dstHeight
     * @param powerOf2  weither we want a power of 2 sclae or not
     * @return
     */
    public static int computeInSampleSize(BitmapFactory.Options options, int dstWidth, int dstHeight, boolean powerOf2) {
        int inSampleSize = 1;

        // Raw height and width of image
        final int srcHeight = options.outHeight;
        final int srcWidth = options.outWidth;

        if (powerOf2) {
            // Find the correct scale value. It should be the power of 2.

            int tmpWidth = srcWidth, tmpHeight = srcHeight;
            while (true) {
                if (tmpWidth / 2 < dstWidth || tmpHeight / 2 < dstHeight)
                    break;
                tmpWidth /= 2;
                tmpHeight /= 2;
                inSampleSize *= 2;
            }
        }
        else

        {
            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) srcHeight / (float) dstHeight);
            final int widthRatio = Math.round((float) srcWidth / (float) dstWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap getRotatedBitmap(String path, Bitmap bitmap){
        Bitmap rotatedBitmap = null;
        Matrix matrix = new Matrix();

        ExifInterface exif = null;
        int orientation = 1;

        try {
            if(path!=null)
            {
                // Getting Exif information of the file
                exif = new ExifInterface(path);

            }
            if(exif!=null)
            {
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                switch(orientation)
                {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.preRotate(270);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.preRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.preRotate(180);
                        break;
                }
                // Rotates the image according to the orientation
                rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotatedBitmap;
    }

    public static int getExifRotation(File imageFile) {
        if (imageFile == null) return 0;
        try {
            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            // We only recognize a subset of orientation tag values
            switch (exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return ExifInterface.ORIENTATION_UNDEFINED;
            }
        } catch (IOException e) {
            com.utilsframework.android.crop.util.Log.e("Error getting Exif data", e);
            return 0;
        }
    }

    public static boolean copyExifRotation(File sourceFile, File destFile) {
        if (sourceFile == null || destFile == null) return false;
        try {
            ExifInterface exifSource = new ExifInterface(sourceFile.getAbsolutePath());
            ExifInterface exifDest = new ExifInterface(destFile.getAbsolutePath());
            exifDest.setAttribute(ExifInterface.TAG_ORIENTATION, exifSource.getAttribute(ExifInterface.TAG_ORIENTATION));
            exifDest.saveAttributes();
            return true;
        } catch (IOException e) {
            com.utilsframework.android.crop.util.Log.e("Error copying Exif data", e);
            return false;
        }
    }

    public static File getFromMediaUri(ContentResolver resolver, Uri uri) {
        if (uri == null) return null;

        if (SCHEME_FILE.equals(uri.getScheme())) {
            return new File(uri.getPath());
        } else if (SCHEME_CONTENT.equals(uri.getScheme())) {
            final String[] filePathColumn = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, filePathColumn, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int columnIndex = (uri.toString().startsWith("content://com.google.android.gallery3d")) ?
                            cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME) :
                            cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                    // Picasa image on newer devices with Honeycomb and up
                    if (columnIndex != -1) {
                        String filePath = cursor.getString(columnIndex);
                        if (!TextUtils.isEmpty(filePath)) {
                            return new File(filePath);
                        }
                    }
                }
            } catch (SecurityException ignored) {
                // Nothing we can do
            } finally {
                if (cursor != null) cursor.close();
            }
        }
        return null;
    }

    public interface OnBitmapReady {
        void onBitmapReady(Bitmap bitmap);
    }

    public static void decodeBitmapArrayAsync(final byte[] array, final OnBitmapReady onBitmapReady){
        new AsyncTask<Void, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... params) {
                return BitmapFactory.decodeByteArray(array, 0, array.length);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                onBitmapReady.onBitmapReady(bitmap);
            }
        }.execute();
    }

    public static Bitmap getBitmapPart(Bitmap bitmap, float x, float y, float width, float height){
        if(width > 1.0f || width <= 0.0f || height > 1.0f || height <= 0.0f || x < 0.0f
                || x >= 1.0f || y < 0.0f || y >= 1.0f){
            throw new IllegalArgumentException("width > 1.0f || width <= 0.0f " +
                    "|| height > 1.0f || height <= 0.0f || x < 0.0f " +
                    "|| x >= 1.0f || y < 0.0f || y >= 1.0f");
        }

        int pixelWidth = bitmap.getWidth();
        int pixelHeight = bitmap.getHeight();
        int newWidth = Math.round(pixelWidth * width);
        int newHeight = Math.round(pixelHeight * height);
        int pixelX = Math.round(x * pixelWidth);
        int pixelY = Math.round(y * pixelWidth);

        return Bitmap.createBitmap(bitmap, pixelX, pixelY, newWidth, newHeight);
    }

    public static void getBitmapPartAsync(final Bitmap bitmap,
                                          final float x,
                                          final float y,
                                          final float width,
                                          final float height,
                                          final OnBitmapReady onBitmapReady){
        new AsyncTask<Void, Void, Bitmap>(){
            @Override
            protected Bitmap doInBackground(Void... params) {
                return getBitmapPart(bitmap, x, y, width, height);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                onBitmapReady.onBitmapReady(bitmap);
            }
        }.execute();
    }

    public static class HSV implements Parcelable, Cloneable{
        public double hue;
        public double saturation;
        public double value;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            HSV hsv = (HSV) o;

            if (Double.compare(hsv.hue, hue) != 0) return false;
            if (Double.compare(hsv.saturation, saturation) != 0) return false;
            if (Double.compare(hsv.value, value) != 0) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(hue);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(saturation);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(value);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }

        public int toColor(){
            return Color.HSVToColor(new float[]{(float) hue, (float) saturation, (float) value});
        }

        @Override
        public String toString() {
            return Math.round(hue) + " " + Math.round(saturation * 100) + " " + Math.round(value * 100);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(hue);
            dest.writeDouble(saturation);
            dest.writeDouble(value);
        }

        public static final Parcelable.Creator<HSV> CREATOR
                = new Parcelable.Creator<HSV>() {
            public HSV createFromParcel(Parcel in) {
                return new HSV(in);
            }

            public HSV[] newArray(int size) {
                return new HSV[size];
            }
        };

        private HSV(Parcel in) {
            hue = in.readDouble();
            saturation = in.readDouble();
            value = in.readDouble();
        }

        public HSV() {

        }

        public HSV(double hue, double saturation, double value) {
            this.hue = hue;
            this.saturation = saturation;
            this.value = value;
        }

        @Override
        public HSV clone() {
            return new HSV(hue, saturation, value);
        }
    }

    public static HSV rgb2hsv(byte red, byte green, byte blue){
        return rgb2hsv((double)red / 255.0, (double)green / 255.0, (double)blue / 255.0);
    }

    public static HSV rgb2hsv(int color){
        float[] result = new float[3];
        Color.colorToHSV(color, result);
        HSV hsv = new HSV();
        hsv.hue = result[0];
        hsv.saturation = result[1];
        hsv.value = result[2];

        return hsv;
    }

    public static HSV rgb2hsv(double red, double green, double blue)
    {
        HSV out = new HSV();
        double min, max, delta;

        min = red < green ? red : green;
        min = min  < blue ? min  : blue;

        max = red > green ? red : green;
        max = max  > blue ? max  : blue;

        out.value = max;
        delta = max - min;
        if( max > 0.0 ) {
            out.saturation = (delta / max);
        } else {
            out.saturation = 0.0;
            out.hue = Double.NaN;
            return out;
        }
        if(red >= max)
            out.hue = (green - blue) / delta;
        else
        if(green >= max)
            out.hue = 2.0 + (blue - red) / delta;
        else
            out.hue = 4.0 + (red - green) / delta;

        out.hue *= 60.0;

        if(out.hue < 0.0)
            out.hue += 360.0;

        return out;
    }

    public static int getAverageColor(int[] pixels){
        long sumBlue = 0;
        long sumRed = 0;
        long sumGreen = 0;

        for(int pixel : pixels){
            sumBlue += Color.blue(pixel);
            sumGreen += Color.green(pixel);
            sumRed += Color.red(pixel);
        }

        int length = pixels.length;
        return Color.rgb((int)Math.round((double)sumRed / length), (int)Math.round((double)sumGreen / length),
                (int)Math.round((double)sumBlue / length));
    }

    public static int getDominantColor(Bitmap bitmap) {
        if (bitmap == null)
            throw new NullPointerException();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];

        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);

        bitmap2.getPixels(pixels, 0, width, 0, 0, width, height);

        final int[][] colorMap = new int[][]{
                new int[256],
                new int[256],
                new int[256]
        };

        List<Integer>[] keys = new List[]{
                new ArrayList<Integer>(256),
                new ArrayList<Integer>(256),
                new ArrayList<Integer>(256)
        };

        int color = 0;
        int r = 0;
        int g = 0;
        int b = 0;

        for (int i = 0; i < pixels.length; i += DOMINANT_CALCULATION_COLOR_STEP) {
            color = pixels[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            if(colorMap[0][r]++ == 0){
                keys[0].add(r);
            }

            if(colorMap[1][g]++ == 0){
                keys[1].add(g);
            }

            if(colorMap[2][b]++ == 0){
                keys[2].add(b);
            }
        }

        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            int max = 0;
            int keyOfMax = 0;

            int[] map = colorMap[i];
            for (int key : keys[i]) {
                int value = map[key];
                if (value > max) {
                    max = value;
                    keyOfMax = key;
                }
            }

            rgb[i] = keyOfMax;
        }

        return Color.rgb(rgb[0], rgb[1], rgb[2]);
    }

    public static int[] getPixels(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        return pixels;
    }
    
    public static int getAverageColor(Bitmap bitmap){
        int[] pixels = getPixels(bitmap);
        return getAverageColor(pixels);
    }

    public static HSV getDominantHSV(Bitmap bitmap){
        int color = getDominantColor(bitmap);
        //int color = getAverageColor(bitmap);
        HSV hsv = rgb2hsv(Color.rgb(230, 200, 146));
        return rgb2hsv(color);
    }

    public static void getDominantHSVAsync(final Bitmap bitmap, final OnFinish<HSV> onFinish){
        new AsyncTask<Void, Void, HSV>(){
            @Override
            protected HSV doInBackground(Void... params) {
                return getDominantHSV(bitmap);
            }

            @Override
            protected void onPostExecute(HSV hsv) {
                onFinish.onFinish(hsv);
            }
        }.execute();
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees){
        if(degrees == 0){
            return bitmap;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static void putBitmapInCenterAndMakeBlackStripes(Canvas canvas, Bitmap bitmap) {
        Paint paint = new Paint();
        paint.setAlpha(255);
        paint.setColor(Color.argb(255, 0, 0, 0));
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        canvas.drawRect(0, 0, width, height, paint);
        float left = (width - bitmap.getWidth()) / 2.0f;
        float top = (height - bitmap.getHeight()) / 2.0f;
        canvas.drawBitmap(bitmap, left, top, paint);
    }

    public static Bitmap putBitmapInCenterAndMakeBlackStripes(Bitmap bitmap, int width, int height) {
        final Bitmap solidBitmap = Bitmap.createBitmap(
                width,
                height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(solidBitmap);
        putBitmapInCenterAndMakeBlackStripes(canvas, bitmap);

        return solidBitmap;
    }

    public static Bitmap decodeFileAndApplyExif(String path) {
        Bitmap result =  BitmapFactory.decodeFile(path);
        int rotation = BitmapUtilities.getExifRotation(new File(path));
        result = BitmapUtilities.rotateBitmap(result, rotation);
        return result;
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Size getBitmapDimensions(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        return new Size(options.outWidth, options.outHeight);
    }

    public static Bitmap getBitmapFromURL(String url) {
        try {
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
    }

    public static Bitmap createSquareBitmapCropToCenter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            int x = (width - height) / 2;
            int y = 0;
            return Bitmap.createBitmap(bitmap, x, y, height, height);
        } else if (height > width) {
            int y = (height - width) / 2;
            int x = 0;
            return Bitmap.createBitmap(bitmap, x, y, width, width);
        } else {
            return bitmap;
        }
    }
}
