package com.rsosor.app.cache;

import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * ICacheStore
 *
 * @author RsosoR
 * @date 2021/9/12
 */
public interface ICacheStore<K ,V> {

    /**
     * Gets by cache key.
     *
     * @param key must not be null
     * @return cache value
     */
    @NonNull
    Optional<V> get(@NonNull K key);

}
