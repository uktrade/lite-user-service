package uk.gov.bis.lite.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import uk.gov.bis.lite.user.service.RedissonCache;

public class RedisModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  public RedissonCache provideRedissonCache(RedissonClient redissonClient) {
    return new RedissonCache(redissonClient, "lite", "user-service");
  }

  @Provides
  @Singleton
  public RedissonClient provideRedissonClient(UserServiceConfiguration userServiceConfiguration) {

    RedisConfiguration redisConfiguration = userServiceConfiguration.getRedisConfiguration();

    boolean useSsl = redisConfiguration.isSsl();
    String protocol = useSsl ? "rediss://" : "redis://"; //add additional "s" to protocol for SSL

    Config redissonConfig = new Config().setCodec(new JsonJacksonCodec(new ObjectMapper()));

    SingleServerConfig singleServerConfig = redissonConfig.useSingleServer()
        .setAddress(protocol + redisConfiguration.getHost() + ":" + redisConfiguration.getPort())
        .setPassword(StringUtils.defaultIfBlank(redisConfiguration.getPassword(), null))
        .setDatabase(redisConfiguration.getDatabase())
        .setTimeout(redisConfiguration.getTimeout())
        .setDnsMonitoring(false)
        .setDnsMonitoringInterval(-1)
        .setConnectionMinimumIdleSize(redisConfiguration.getPoolMinIdle())
        .setConnectionPoolSize(redisConfiguration.getPoolMaxTotal());

    if (useSsl) {
      //Don't attempt to verify the SSL certificates
      singleServerConfig.setSslEnableEndpointIdentification(false);
    }

    return Redisson.create(redissonConfig);
  }


}
