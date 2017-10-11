package uk.gov.bis.lite.user.service;

import uk.gov.bis.lite.user.api.view.UserPrivilegesView;

import java.util.Optional;

public interface UserPrivilegesService {

  Optional<UserPrivilegesView> getUserPrivileges(String userId);

}
