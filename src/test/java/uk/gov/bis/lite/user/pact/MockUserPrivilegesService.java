package uk.gov.bis.lite.user.pact;

import com.google.inject.Singleton;
import uk.gov.bis.lite.user.api.view.CustomerView;
import uk.gov.bis.lite.user.api.view.SiteView;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.api.view.enums.Role;
import uk.gov.bis.lite.user.service.UserPrivilegesService;

import java.util.Collections;
import java.util.Optional;

@Singleton
public class MockUserPrivilegesService implements UserPrivilegesService {

  private boolean userExists;

  @Override
  public Optional<UserPrivilegesView> getUserPrivileges(String userId) {
    if (userExists) {
      CustomerView customerView = new CustomerView();
      customerView.setCustomerId("CUSTOMER_289");
      customerView.setRole(Role.SUBMITTER);
      SiteView siteView = new SiteView();
      siteView.setSiteId("SITE_887");
      siteView.setRole(Role.SUBMITTER);
      UserPrivilegesView userPrivilegesView = new UserPrivilegesView();
      userPrivilegesView.setCustomers(Collections.singletonList(customerView));
      userPrivilegesView.setSites(Collections.singletonList(siteView));
      return Optional.of(userPrivilegesView);
    } else {
      return Optional.empty();
    }
  }

  public void setUserExists(boolean userExists) {
    this.userExists = userExists;
  }

}
