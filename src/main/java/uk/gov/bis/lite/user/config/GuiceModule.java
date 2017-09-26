package uk.gov.bis.lite.user.config;

import com.google.inject.AbstractModule;
import ru.vyarus.dropwizard.guice.module.support.ConfigurationAwareModule;
import uk.gov.bis.lite.common.metrics.readiness.DefaultReadinessService;
import uk.gov.bis.lite.common.metrics.readiness.ReadinessService;
import uk.gov.bis.lite.user.service.UserPrivilegesService;
import uk.gov.bis.lite.user.service.UserPrivilegesServiceImpl;

public class GuiceModule extends AbstractModule implements ConfigurationAwareModule<UserServiceConfiguration> {

  private UserServiceConfiguration config;

  @Override
  protected void configure() {
    bind(ReadinessService.class).to(DefaultReadinessService.class);
    bind(UserPrivilegesService.class).to(UserPrivilegesServiceImpl.class);
  }

  @Override
  public void setConfiguration(UserServiceConfiguration config) {
    this.config = config;
  }
}
