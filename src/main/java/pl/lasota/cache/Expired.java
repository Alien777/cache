package pl.lasota.cache;

public interface Expired<K, V> extends Cache<K, V> {

    void add(K k, V v, long maxLife);

    void extendValidity(K k);
}
