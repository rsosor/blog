package com.rsosor.app.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rsosor.app.utils.JsonUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * AbstractStringCacheStore
 *
 * @author RsosoR
 * @date 2021/9/16
 */
public abstract class AbstractStringCacheStore extends AbstractCacheStore<String, String> {

    protected Optional<CacheWrapper<String>> jsonToCacheWrapper(String json) {
        Assert.hasText(json, "json value must not be null");
        CacheWrapper<String> cacheWrapper = null;
        try {
            cacheWrapper = JsonUtils.jsonToObject(json, CacheWrapper.class);
        } catch (IOException e) {
            log.debug("Failed to convert json to wrapper value bytes: [{}]", json, e);
        }
        return Optional.ofNullable(cacheWrapper);
    }

    public <T> void putAny(String key, T value) {
        try {
            put(key, JsonUtils.objectToJson(value));
        } catch (JsonProcessingException e) {
            throw new ServiceException("Failed to convert " + value + " to json", e);
        }
    }

    public <T> void putAny(@NonNull String key, @NonNull T value, long timeout,
                           @NonNull TimeUnit timeUnit) {
        try {
            put(key, JsonUtils.objectToJson(value), timeout, timeUnit);
        } catch (JsonProcessingException e) {
            throw new ServiceException("Failed to convert " + value + " to json", e);
        }
    }

    public <T> Optional<T> getAny(String key, Class<T> type) {
        Assert.notNull(type, "Type must not be null");

        return get(key).map(value -> {
            try {
                return JsonUtils.jsonToObject(value, type);
            } catch (IOException e) {
                log.error("Failed to convert json to type: " + type.getName(), e);
                return null;
            }
        });
    }
}
