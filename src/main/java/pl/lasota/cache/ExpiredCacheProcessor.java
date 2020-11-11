package pl.lasota.cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;


final class ExpiredCacheProcessor<K, V> extends SimpleCache<Expired<K>, Expired<V>> {

    ExpiredCacheProcessor(ConcurrentMap<Expired<K>, Expired<V>> cache) {
        super(cache);
    }

    @Override
    public Optional<Expired<V>> get(Expired<K> k) {
        Optional<Expired<V>> v = super.get(k);
        if (v.isPresent()) {
            if (v.get().isExpired()) {
                remove(k);
                return Optional.empty();
            } else {
                return v;
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean containsKey(Expired<K> k) {
        Optional<Expired<V>> v = super.get(k);
        if (v.isPresent()) {
            if (v.get().isExpired()) {
                remove(k);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    void cleanup() {
        super.forEach((kExpired, v) -> {
            if (kExpired.isExpired()) {
                remove(kExpired);
            }
        });
    }
}
