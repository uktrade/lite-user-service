package uk.gov.bis.lite.user;

import com.google.inject.AbstractModule;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.service.UserDetailsServiceImpl;
import uk.gov.bis.lite.user.service.UserPrivilegesService;
import uk.gov.bis.lite.user.service.UserPrivilegesServiceImpl;

public class TestServiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(UserPrivilegesService.class).to(UserPrivilegesServiceImpl.class);
    bind(UserDetailsService.class).to(UserDetailsServiceImpl.class);
  }
}
