package pl.lasota.cache;

import java.util.concurrent.TimeUnit;

final class ExpiredCreator<K, V> {

    private final long life;

    ExpiredCreator(long maxLife, TimeUnit timeUnit) {
        life = System.currentTimeMillis() + timeUnit.toMillis(maxLife);
    }

    Expired<K> getKey(K k) {
        return new Expired<>(k, life);
    }

    Expired<V> getValue(V v) {
        return new Expired<>(v, life);
    }
}
