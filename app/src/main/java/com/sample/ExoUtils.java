package com.sample;

import android.app.Activity;

/**
 * Created by cisner-2 on 20/2/18.
 */

public class ExoUtils {

    private static CacheDataSourceFactory cache;

    public static CacheDataSourceFactory getCacheData(Activity mActivity) {
        if (cache == null)
            cache = new CacheDataSourceFactory(mActivity, 100 * 1024 * 1024, 5 * 1024 * 1024);
        return cache;
    }
}
