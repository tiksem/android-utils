package com.utilsframework.android.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.utils.framework.strings.Strings;

import java.io.FileNotFoundException;
import java.io.IOException;

/*This shit doesn't not work well. Use this lib https://github.com/commonsguy/cwac-provider*/
public class AssetsContentProvider extends ContentProvider {
    public static final String TAG = AssetsContentProvider.class.getSimpleName();

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        Context context = getContext();
        if (context == null) {
            throw new RuntimeException("content == null");
        }

        AssetManager assetManager = context.getAssets();
        String filePath = uri.getPath();

        if (filePath != null) {
            filePath = Strings.removeFromStart(filePath, "/");
        }
        if(Strings.isEmpty(filePath)) {
            Log.e(TAG, "filePath is empty, invalid uri");
            throw new FileNotFoundException("filePath is empty, invalid uri");
        }

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = assetManager.openFd(filePath);
        } catch (IOException e) {
            Log.e(TAG, "assetManager.openFd failed", e);
        }
        return fileDescriptor;
    }
}
