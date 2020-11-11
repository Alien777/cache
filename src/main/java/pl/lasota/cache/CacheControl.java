package pl.lasota.cache;

import java.util.concurrent.ConcurrentHashMap;

public class CacheControl {
    private static volatile CacheControl INSTANCE;

    private final ConcurrentHashMap<String, Cache> caches;

    private CacheControl() {
        this.caches = new ConcurrentHashMap<>();
    }

    public static CacheControl instance() {
        if (INSTANCE == null) {
            synchronized (CacheControl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CacheControl();
                }
            }
        }
        return INSTANCE;
    }

    public CacheControl addCache(String id, Cache cache) {
        caches.put(id, cache);
        return INSTANCE;
    }

    public Cache getCache(String id) {
        return caches.get(id);
    }

}



