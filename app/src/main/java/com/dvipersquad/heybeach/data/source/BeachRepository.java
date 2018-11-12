package com.dvipersquad.heybeach.data.source;

import android.support.annotation.NonNull;

import com.dvipersquad.heybeach.data.Beach;

import java.util.List;

public class BeachRepository implements BeachDataSource {

    private static BeachRepository INSTANCE = null;

    private BeachDataSource imageRemoteDataSource;

    private BeachRepository(@NonNull BeachDataSource imageRemoteDataSource) {
        this.imageRemoteDataSource = imageRemoteDataSource;
    }

    public static BeachRepository getInstance(BeachDataSource imageRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new BeachRepository(imageRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getBeaches(final LoadBeachesCallback callback) {
        imageRemoteDataSource.getBeaches(new LoadBeachesCallback() {
            @Override
            public void onBeachesLoaded(List<Beach> beaches) {
                callback.onBeachesLoaded(beaches);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}
