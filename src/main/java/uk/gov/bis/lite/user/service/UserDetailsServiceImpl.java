package uk.gov.bis.lite.user.service;

import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import uk.gov.bis.lite.common.spire.client.SpireRequest;
import uk.gov.bis.lite.user.spire.SpireUserNotFoundException;
import uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetails;
import uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsClient;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {

  private final SpireUserDetailsClient spireUserDetailsClient;

  @Inject
  public UserDetailsServiceImpl(SpireUserDetailsClient spireUserDetailsClient) {
    this.spireUserDetailsClient = spireUserDetailsClient;
  }

  @Override
  public Optional<SpireUserDetails> getUserDetails(String userId) {
    if (StringUtils.isBlank(userId) || StringUtils.length(userId) > 10) {
      throw new UserDetailsServiceException(String.format("Supplied user id is invalid %s", userId));
    }

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
