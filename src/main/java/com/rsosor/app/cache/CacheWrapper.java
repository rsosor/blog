package com.rsosor.app.cache;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * CacheWrapper
 *
 * @author RsosoR
 * @date 2021/9/16
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
class CacheWrapper<V> implements Serializable {

    /**
     * Cache data
     */
    private V data;

    /**
     * Expired time.
     */
    private Date expireAt;

    /**
     * Create time.
     */
    private Date createAt;
}
