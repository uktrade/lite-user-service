package uk.gov.bis.lite.user.service;

import uk.gov.bis.lite.user.api.CustomerView;
import uk.gov.bis.lite.user.api.SiteView;
import uk.gov.bis.lite.user.api.UserPrivilegesView;

import java.util.Optional;

public class UserPrivilegesServiceImpl implements UserPrivilegesService {
  @Override
  public Optional<UserPrivilegesView> getUserPrivileges(String userId) {
    UserPrivilegesView userPrivs = UserPrivilegesView.builder()
        .setUserAccountType("APPLICANT")
        .addCustomer(new CustomerView("SITE123", "ADMIN"))
        .addSite(new SiteView("SITE123", "PREPARER"))
        .build();
    return Optional.of(userPrivs);
  }
}
