package uk.gov.bis.lite.user.config;

import com.google.inject.AbstractModule;
import ru.vyarus.dropwizard.guice.module.support.ConfigurationAwareModule;
import uk.gov.bis.lite.common.metrics.readiness.DefaultReadinessService;
import uk.gov.bis.lite.common.metrics.readiness.ReadinessService;

public class GuiceModule extends AbstractModule implements ConfigurationAwareModule<UserServiceConfiguration> {

  private UserServiceConfiguration config;

  @Override
  protected void configure() {
    bind(ReadinessService.class).to(DefaultReadinessService.class);
  }

  @Override
  public void setConfiguration(UserServiceConfiguration config) {
    this.config = config;
  }
}
