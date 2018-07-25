package uk.gov.bis.lite.user.pact;

import com.google.inject.AbstractModule;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.service.UserPrivilegesService;

public class PactServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(UserPrivilegesService.class).to(MockUserPrivilegesService.class);
    bind(UserDetailsService.class).to(MockUserDetailsService.class);
  }

}
