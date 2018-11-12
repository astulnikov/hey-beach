package com.dvipersquad.heybeach.util.imageloading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dvipersquad.heybeach.util.AppExecutors;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CustomImageLoader implements ImageLoader {

    private AppExecutors appExecutors;

    public CustomImageLoader(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    @Override
    public void loadImage(final String url, final LoadImageCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = getBitmapFromUrl(url);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (bitmap == null) {
                            // This will be called if the table is new or just empty.
                            callback.onImageNotAvailable();
                        } else {
                            callback.onImageLoaded(bitmap);
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
}
