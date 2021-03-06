package uk.gov.bis.lite.user.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import uk.gov.bis.lite.common.redis.RedissonCache;
import uk.gov.bis.lite.common.redis.Ttl;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetails;

import java.util.Optional;

public class RedisUserDetailsServiceImpl implements UserDetailsService {

  private final UserDetailsServiceImpl userDetailsServiceImpl;
  private final RedissonCache redissonCache;
  private final Ttl getUserDetailsTtl;

  @Inject
  public RedisUserDetailsServiceImpl(UserDetailsServiceImpl userDetailsServiceImpl,
                                     RedissonCache redissonCache,
                                     @Named("getUserDetailsTtl") Ttl getUserDetailsTtl) {
    this.userDetailsServiceImpl = userDetailsServiceImpl;
    this.redissonCache = redissonCache;
    this.getUserDetailsTtl = getUserDetailsTtl;
  }

  @Override
  public Optional<SpireUserDetails> getUserDetails(String userId) {
    return redissonCache.getOptional(() -> userDetailsServiceImpl.getUserDetails(userId),
        "getUserDetails",
        getUserDetailsTtl,
        userId);
  }

}
