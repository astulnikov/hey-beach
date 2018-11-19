package com.dvipersquad.heybeach;

import com.dvipersquad.heybeach.auth.provider.AuthProvider;
import com.dvipersquad.heybeach.auth.provider.CustomAuthProvider;
import com.dvipersquad.heybeach.data.source.BeachRepository;
import com.dvipersquad.heybeach.data.source.remote.BeachRemoteDataSource;
import com.dvipersquad.heybeach.util.AppExecutors;
import com.dvipersquad.heybeach.util.imageloading.CustomImageLoader;
import com.dvipersquad.heybeach.util.imageloading.ImageLoader;

public class Injection {
    public static BeachRepository provideBeachRepository() {
        return BeachRepository.getInstance(BeachRemoteDataSource.getInstance(new AppExecutors()));
    }

    public static ImageLoader provideImageLoader() { // TODO instances creation control
        return new CustomImageLoader(new AppExecutors());
    }

    public static AuthProvider provideAuthProvider() {
        return CustomAuthProvider.getInstance(new AppExecutors());
    }
}
