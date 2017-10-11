package uk.gov.bis.lite.user.service;

import com.google.inject.Inject;
import uk.gov.bis.lite.common.spire.client.SpireRequest;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.spire.SpireUserRolesAdapter;
import uk.gov.bis.lite.user.spire.SpireUserRolesClient;
import uk.gov.bis.lite.user.spire.SpireUserRolesUserNotFoundException;

import java.util.Optional;

public class UserPrivilegesServiceImpl implements UserPrivilegesService {

  private final SpireUserRolesClient spireUserRolesClient;

  @Inject
  public UserPrivilegesServiceImpl(SpireUserRolesClient spireUserRolesClient) {
    this.spireUserRolesClient = spireUserRolesClient;
  }

  @Override
  public Optional<UserPrivilegesView> getUserPrivileges(String userId) {
    SpireRequest request = spireUserRolesClient.createRequest();
    request.addChild("userId", userId);
    try {
      return Optional.of(spireUserRolesClient.sendRequest(request)).map(SpireUserRolesAdapter::adapt);
    }
    catch (SpireUserRolesUserNotFoundException e) {
      return Optional.empty();
    }
  }
}
