package pl.lasota.cache;

import java.util.Optional;

public interface Cache<K, V> {

    void add(K k, V v);

    Optional<V> get(K k);

    void remove(K k);

    boolean containsKey(K k);

    long size();

    void cleanup();

}
