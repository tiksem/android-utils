package com.dbbest.android.image.listener;

import android.net.Uri;

public interface ImagePicked {
    void onImagePicked(int sourceType, Uri pickedImage);
}
