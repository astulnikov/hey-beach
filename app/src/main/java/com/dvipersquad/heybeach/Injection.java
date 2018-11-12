package com.dvipersquad.heybeach;

import com.dvipersquad.heybeach.data.source.BeachRepository;
import com.dvipersquad.heybeach.data.source.remote.BeachRemoteDataSource;
import com.dvipersquad.heybeach.util.AppExecutors;
import com.dvipersquad.heybeach.util.imageloading.CustomImageLoader;
import com.dvipersquad.heybeach.util.imageloading.ImageLoader;

public class Injection {
    public static BeachRepository provideBeachRepository() {
        return BeachRepository.getInstance(BeachRemoteDataSource.getInstance(new AppExecutors()));
    }

    public static ImageLoader provideImageLoader() {
        return new CustomImageLoader(new AppExecutors());
    }
}
