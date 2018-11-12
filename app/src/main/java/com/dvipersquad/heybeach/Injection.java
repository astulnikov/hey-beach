package com.dvipersquad.heybeach;

import com.dvipersquad.heybeach.data.source.BeachRepository;
import com.dvipersquad.heybeach.data.source.remote.BeachRemoteDataSource;
import com.dvipersquad.heybeach.util.AppExecutors;

public class Injection {
    public static BeachRepository provideImageRepository() {
        return BeachRepository.getInstance(BeachRemoteDataSource.getInstance(new AppExecutors()));
    }
}
