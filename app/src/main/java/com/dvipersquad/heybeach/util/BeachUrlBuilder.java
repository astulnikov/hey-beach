package com.dvipersquad.heybeach.util;

import com.dvipersquad.heybeach.BuildConfig;

public class BeachUrlBuilder {

    public static String generate(String url) {
        return BuildConfig.baseApiUrl + url;
    }
}
