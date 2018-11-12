package com.dvipersquad.heybeach.data.source;

import com.dvipersquad.heybeach.data.Beach;

import java.util.List;

public interface BeachDataSource {

    interface LoadBeachesCallback {

        void onBeachesLoaded(List<Beach> beaches);

        void onDataNotAvailable();
    }

    void getBeaches(LoadBeachesCallback callback);

}
