package smt666.mall.service;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import smt666.common.beans.CacheKeyConstants;
import smt666.common.util.JsonMapper;

import javax.annotation.Resource;

/**
 * @author 27140
 */
@Service
@Slf4j
public class SysCacheService {

    @Resource(name = "redisPool")
    private RedisPool redisPool;

    /**
     * 保存到redis中的方法。
     * @param toSavedValue 要进行缓存的数据内容。
     * @param timeoutSeconds 数据过期的时间（秒）。
     * @param prefix 缓存的数据Value对应的Key的前缀（方便对同一类前缀的数据进行管理）。我们特意自定义了一个管理前缀的枚举。
     */
    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix) {
        saveCache(toSavedValue, timeoutSeconds, prefix, null);
    }

    /**
     * 为了适配redis的API来封装准备的方法，可以保存多个值。
     * @param toSavedValue
     * @param timeoutSeconds
     * @param prefix
     * @param keys
     */
    public void saveCache(String toSavedValue, int timeoutSeconds, CacheKeyConstants prefix, String... keys) {
        if (toSavedValue == null) {
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix, keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey, timeoutSeconds, toSavedValue);
        } catch (Exception e) {
            log.error("save cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.objectToString(keys), e);
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    public String getFromCache(CacheKeyConstants prefix, String... keys) {
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix, keys);
        try {
            shardedJedis = redisPool.instance();
            String value = shardedJedis.get(cacheKey);
            return value;
        } catch (Exception e) {
            log.error("get from cache exception, prefix:{}, keys:{}", prefix.name(), JsonMapper.objectToString(keys), e);
            return null;
        } finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 每次处理的时候，我们都会生成对应的缓存的权限点的名称的key的方法。
     * @param prefix
     * @param keys
     * @return
     */
    private String generateCacheKey(CacheKeyConstants prefix, String... keys) {
        String key = prefix.name();
        if (keys != null && keys.length > 0) {
            /*  Joiner 类（Java1.8的高级类之一）。
            对于一个集合的操作，它可以通过调用Joiner.on()方式把每一项集合中的元素给拆开。
            此处表示，如果我们有多个key值传入，那么可以直接使用"_"下划线进行拼接成一个key 。
             */
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
