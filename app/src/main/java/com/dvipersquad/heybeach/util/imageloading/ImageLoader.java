package com.dvipersquad.heybeach.util.imageloading;

import android.graphics.Bitmap;

public interface ImageLoader {

    interface LoadImageCallback {
        void onImageLoaded(Bitmap bitmap);

        void onImageNotAvailable();
    }

    void loadImage(String url, LoadImageCallback callback);
}
