package uk.gov.bis.lite.user.service;

import com.google.inject.Inject;
import uk.gov.bis.lite.common.spire.client.SpireRequest;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.spire.SpireUserNotFoundException;
import uk.gov.bis.lite.user.spire.user.roles.SpireUserRolesAdapter;
import uk.gov.bis.lite.user.spire.user.roles.SpireUserRolesClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class UserPrivilegesServiceImpl implements UserPrivilegesService {

  private final SpireUserRolesClient spireUserRolesClient;
  private final RedissonCache redissonCache;

  @Inject
  public UserPrivilegesServiceImpl(SpireUserRolesClient spireUserRolesClient,
                                   RedissonCache redissonCache) {
    this.spireUserRolesClient = spireUserRolesClient;
    this.redissonCache = redissonCache;
  }

  @Override
  public Optional<UserPrivilegesView> getUserPrivileges(String userId) {
    return Optional.of(redissonCache.get(() -> sendUserPrivilegesRequest(userId).orElse(null), "getUserPrivileges", 1,
        TimeUnit.DAYS, userId));
  }

  private Optional<UserPrivilegesView> sendUserPrivilegesRequest(String userId) {
    SpireRequest request = spireUserRolesClient.createRequest();
    request.addChild("userId", userId);
    try {
      return Optional.of(spireUserRolesClient.sendRequest(request)).map(SpireUserRolesAdapter::adapt);
    } catch (SpireUserNotFoundException e) {
      return Optional.empty();
    }
  }
}
