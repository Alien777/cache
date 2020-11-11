package pl.lasota.cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ExpiredCache<K, V> implements Cache<K, V> {

    private final ExpiredCacheProcessor<K, V> expiredCacheProcessor = new ExpiredCacheProcessor<>(new ConcurrentHashMap<>());
    private final TimeUnit timeUnit;
    private final long maxLife;

    public ExpiredCache(long maxLife, TimeUnit timeUnit) {
        this.maxLife = maxLife;
        this.timeUnit = timeUnit;
    }

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
        Optional<Expired<V>> vExpired = expiredCacheProcessor.get(new Expired<>(k));
        return vExpired.map(Expired::getValue);
    }

    @Override
    public void remove(K k) {
        expiredCacheProcessor.remove(new Expired<>(k));
    }

    @Override
    public boolean containsKey(K k) {
        return expiredCacheProcessor.containsKey(new Expired<>(k));
    }

    public long size() {
        return expiredCacheProcessor.size();
    }

    public void cleanup() {
        expiredCacheProcessor.cleanup();
    }
}
