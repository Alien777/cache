package pl.lasota.cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ExpiredCache<K, V> implements Expired<K, V> {

    private final ExpiredCacheProcessor<K, V> expiredCacheProcessor = new ExpiredCacheProcessor<>(new ConcurrentHashMap<>());
    private final TimeUnit timeUnit;
    private final long maxLife;

    public ExpiredCache(long maxLife, TimeUnit timeUnit) {
        this.maxLife = maxLife;
        this.timeUnit = timeUnit;
    }

    @Override
    public void add(K k, V v, long maxLife) {
        final ExpiredCreator<K, V> creator = new ExpiredCreator<>(maxLife, timeUnit);
        expiredCacheProcessor.add(creator.getKey(k), creator.getValue(v));
    }

    @Override
    public void add(K k, V v) {
        final ExpiredCreator<K, V> creator = new ExpiredCreator<>(maxLife, timeUnit);
        expiredCacheProcessor.add(creator.getKey(k), creator.getValue(v));
    }

    @Override
    public Optional<V> get(K k) {
        Optional<ExpiredField<V>> vExpired = expiredCacheProcessor.get(new ExpiredField<>(k));
        return vExpired.map(ExpiredField::getValue);
    }

    @Override
    public void extendValidity(K k) {
        Optional<V> v = get(k);
        remove(k);
        v.ifPresent(v1 -> add(k, v1));
    }

    @Override
    public void remove(K k) {
        expiredCacheProcessor.remove(new ExpiredField<>(k));
    }

    @Override
    public boolean containsKey(K k) {
        return expiredCacheProcessor.containsKey(new ExpiredField<>(k));
    }

    @Override
    public long size() {
        return expiredCacheProcessor.size();
    }

    @Override
    public void cleanup() {
        expiredCacheProcessor.cleanup();
    }

}
