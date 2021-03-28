package pl.lasota.cache;

import java.util.concurrent.TimeUnit;

final class ExpiredCreator<K, V> {

    private final long life;

    ExpiredCreator(long maxLife, TimeUnit timeUnit) {
        life = System.currentTimeMillis() + timeUnit.toMillis(maxLife);
    }

    ExpiredField<K> getKey(K k) {
        return new ExpiredField<>(k, life);
    }

    ExpiredField<V> getValue(V v) {
        return new ExpiredField<>(v, life);
    }
}
