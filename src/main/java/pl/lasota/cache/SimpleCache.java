package pl.lasota.cache;

import java.util.function.BiConsumer;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public class SimpleCache<K, V> implements Cache<K, V> {

    private final ConcurrentMap<K, V> cache;

    public SimpleCache(ConcurrentMap<K, V> cache) {
        this.cache = cache;
    }

    @Override
    public void add(K k, V v) {
        cache.putIfAbsent(k, v);
    }

    @Override
    public Optional<V> get(K k) {
        V v = cache.get(k);
        if (v == null) {
            return Optional.empty();
        }
        return Optional.of(v);
    }

    @Override
    public void remove(K k) {
        if (k == null) {
            return;
        }
        cache.remove(k);
    }

    @Override
    public boolean containsKey(K k) {
        return cache.containsKey(k);
    }

    void cleanup() {
        cache.clear();
    }

    void forEach(BiConsumer<K, V> consumer) {
        cache.forEach(consumer);
    }

    long size() {
        return cache.size();
    }
}
