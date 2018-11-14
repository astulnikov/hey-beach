package com.dvipersquad.heybeach.util.imageloading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.dvipersquad.heybeach.util.AppExecutors;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CustomImageLoader implements ImageLoader {

    private LruCache<String, Bitmap> mMemoryCache;

    private AppExecutors appExecutors;

    public CustomImageLoader(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public void loadImage(final String url, final LoadImageCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromMemCache(url);
                if (bitmap == null) {
                    bitmap = getBitmapFromUrl(url);
                    if (bitmap != null) {
                        addBitmapToMemoryCache(url, bitmap);
                    }
                }
                final Bitmap finalBitmap = bitmap;
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (finalBitmap == null) {
                            // This will be called if the table is new or just empty.
                            callback.onImageNotAvailable();
                        } else {
                            callback.onImageLoaded(finalBitmap);
                        }
                    }
                });
            }
        };

        appExecutors.networkIO().execute(runnable);
    }

    private Bitmap getBitmapFromUrl(String imageUri) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(imageUri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream is = connection.getInputStream();

            return BitmapFactory.decodeStream(is);


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}
