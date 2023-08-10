package com.gpd.gpdimg.activity;

import androidx.collection.LruCache;

/**
 * Created by Jithu Sunny on 30,April,2020
 * OGES iNFOTECH
 */
public class Cache {
    private static Cache instance;
    private LruCache<Object, Object> lru;

    private Cache() {

        lru = new LruCache<Object, Object>(1024);

    }

    public static Cache getInstance() {

        if (instance == null) {

            instance = new Cache();
        }

        return instance;

    }

    public LruCache<Object, Object> getLru() {
        return lru;
    }
}
