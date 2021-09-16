package com.rsosor.app.cache;

import com.rsosor.app.config.properties.RsosoRProperties;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.DB;

import java.util.Timer;

/**
 * level-db cache store
 *
 * @author RsosoR
 * @date 2021/9/16
 */
@Slf4j
public class LevelCacheStore extends AbstractStringCacheStore {

    /**
     * Cleaner schedule period. (ms)
     */
    private static final long PERIOD = 60 * 1000;

    private static DB LEVEL_DB;

    private Timer timer;

    public LevelCacheStore(RsosoRProperties rsosorProperties) {
        super.rsosorProperties = rsosorProperties;
    }


}
