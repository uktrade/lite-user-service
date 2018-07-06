package uk.gov.bis.lite.user.service;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RedissonCache {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedissonCache.class);

  private final RedissonClient redissonClient;
  private final String prefix;
  private final String key;

  @Inject
  public RedissonCache(RedissonClient redissonClient, String prefix, String key) {
    this.redissonClient = redissonClient;
    this.prefix = prefix;
    this.key = key;
  }

  public <T> T get(Supplier<T> supplier, String method, long ttl, TimeUnit timeUnit, String... arguments) {
    String hashKey = hashKey(method, arguments);
    RBucket<T> rBucket = redissonClient.getBucket(hashKey);
    T cachedObject = rBucket.get();
    if (cachedObject != null) {
      LOGGER.info("returned cached object {}", hashKey);
      return cachedObject;
    } else {
      try {
        T object = supplier.get();
        rBucket.set(object, ttl, timeUnit);
        return object;
      } catch (Exception exception) {
        LOGGER.error("Unable to get object {}", hashKey, exception);
        return null;
      }
    }
  }

  private String hashKey(String method, String... arguments) {
    return prefix + ":" + key + ":" + method + ":" + StringUtils.join(arguments, ":");
  }

}
