package uk.gov.bis.lite.user.service;

import com.google.inject.Inject;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.spire.SpireUserRolesAdapter;
import uk.gov.bis.lite.user.spire.SpireUserRolesClient;

import java.util.Optional;

public class UserPrivilegesServiceImpl implements UserPrivilegesService {

  private final SpireUserRolesClient spireUserRolesClient;

  @Inject
  public UserPrivilegesServiceImpl(SpireUserRolesClient spireUserRolesClient) {
    this.spireUserRolesClient = spireUserRolesClient;
  }

  @Override
  public Optional<UserPrivilegesView> getUserPrivileges(String userId) {
    return spireUserRolesClient.sendRequest(userId).map(SpireUserRolesAdapter::adapt);
  }
}
