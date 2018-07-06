package uk.gov.bis.lite.user.service;

import com.google.inject.Inject;
import uk.gov.bis.lite.common.spire.client.SpireRequest;
import uk.gov.bis.lite.user.spire.SpireUserNotFoundException;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetails;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class UserDetailsServiceImpl implements UserDetailsService {

  private final SpireUserDetailsClient spireUserDetailsClient;
  private final RedissonCache redissonCache;

  @Inject
  public UserDetailsServiceImpl(SpireUserDetailsClient spireUserDetailsClient, RedissonCache redissonCache) {
    this.spireUserDetailsClient = spireUserDetailsClient;
    this.redissonCache = redissonCache;
  }

  @Override
  public Optional<SpireUserDetails> getUserDetails(String userId) {
    return Optional.of(redissonCache.get(() -> sendUserDetailsRequest(userId).orElse(null), "getUserDetails", 1,
        TimeUnit.DAYS, userId));
  }

  private Optional<SpireUserDetails> sendUserDetailsRequest(String userId) {
    SpireRequest request = spireUserDetailsClient.createRequest();
    request.addChild("VERSION_NO", "1.0");
    request.addChild("WUA_ID", userId);

    try {
      return Optional.of(spireUserDetailsClient.sendRequest(request));
    } catch (SpireUserNotFoundException e) {
      return Optional.empty();
    }
  }
}
