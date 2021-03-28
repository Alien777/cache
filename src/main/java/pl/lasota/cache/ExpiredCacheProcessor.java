package pl.lasota.cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;


final class ExpiredCacheProcessor<K, V> extends SimpleCache<ExpiredField<K>, ExpiredField<V>> {

    ExpiredCacheProcessor(ConcurrentMap<ExpiredField<K>, ExpiredField<V>> cache) {
        super(cache);
    }

    @Override
    public Optional<ExpiredField<V>> get(ExpiredField<K> k) {
        Optional<ExpiredField<V>> v = super.get(k);
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
    public boolean containsKey(ExpiredField<K> k) {
        Optional<ExpiredField<V>> v = super.get(k);
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

    @Override
    public void cleanup() {
        super.forEach((kExpiredField, v) -> {
            if (kExpiredField.isExpired()) {
                remove(kExpiredField);
            }
        });
    }
}
