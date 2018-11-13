package com.dvipersquad.heybeach.data.source;

import android.support.annotation.NonNull;

import com.dvipersquad.heybeach.data.Beach;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BeachRepository implements BeachDataSource {

    private static BeachRepository INSTANCE = null;

    private BeachDataSource imageRemoteDataSource;

    private Map<String, Beach> cachedBeaches;

    private boolean cacheIsDirty = false;

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
        if (cachedBeaches != null && !cacheIsDirty) {
            callback.onBeachesLoaded(new ArrayList<>(cachedBeaches.values()));
            return;
        }
        imageRemoteDataSource.getBeaches(new LoadBeachesCallback() {
            @Override
            public void onBeachesLoaded(List<Beach> beaches) {
                refreshCache(beaches);
                callback.onBeachesLoaded(beaches);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getBeachesNextPage(int page, final LoadBeachesCallback callback) {
        imageRemoteDataSource.getBeachesNextPage(page, new LoadBeachesCallback() {
            @Override
            public void onBeachesLoaded(List<Beach> beaches) {
                refreshCache(beaches);
                callback.onBeachesLoaded(beaches);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Beach> beaches) {
        if (cachedBeaches == null) {
            cachedBeaches = new LinkedHashMap<>();
        }
        if (cacheIsDirty) {
            cachedBeaches.clear();
        }
        for (Beach beach : beaches) {
            cachedBeaches.put(beach.getId(), beach);
        }
    }
}
