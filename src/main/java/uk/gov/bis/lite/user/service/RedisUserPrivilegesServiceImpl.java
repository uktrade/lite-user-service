package uk.gov.bis.lite.user.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import uk.gov.bis.lite.common.redis.RedissonCache;
import uk.gov.bis.lite.common.redis.Ttl;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;

import java.util.Optional;

public class RedisUserPrivilegesServiceImpl implements UserPrivilegesService {

  private final UserPrivilegesServiceImpl userPrivilegesServiceImpl;
  private final RedissonCache redissonCache;
  private final Ttl getUserPrivileges;

  @Inject
  public RedisUserPrivilegesServiceImpl(UserPrivilegesServiceImpl userPrivilegesServiceImpl,
                                        RedissonCache redissonCache,
                                        @Named("getUserPrivileges") Ttl getUserPrivileges) {
    this.userPrivilegesServiceImpl = userPrivilegesServiceImpl;
    this.redissonCache = redissonCache;
    this.getUserPrivileges = getUserPrivileges;
  }

  @Override
  public Optional<UserPrivilegesView> getUserPrivileges(String userId) {
    return redissonCache.getOptional(() -> userPrivilegesServiceImpl.getUserPrivileges(userId),
        "getUserPrivileges",
        getUserPrivileges,
        userId);
  }

}
