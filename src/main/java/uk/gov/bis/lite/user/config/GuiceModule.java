package uk.gov.bis.lite.user.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import ru.vyarus.dropwizard.guice.module.support.ConfigurationAwareModule;
import uk.gov.bis.lite.common.metrics.readiness.DefaultReadinessService;
import uk.gov.bis.lite.common.metrics.readiness.ReadinessService;
import uk.gov.bis.lite.common.spire.client.SpireClientConfig;
import uk.gov.bis.lite.common.spire.client.SpireRequestConfig;
import uk.gov.bis.lite.common.spire.client.errorhandler.ErrorNodeErrorHandler;
import uk.gov.bis.lite.user.service.UserPrivilegesService;
import uk.gov.bis.lite.user.service.UserPrivilegesServiceImpl;
import uk.gov.bis.lite.user.spire.SpireUserRolesClient;
import uk.gov.bis.lite.user.spire.SpireUserRolesErrorHandler;
import uk.gov.bis.lite.user.spire.SpireUserRolesParser;

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

  @Provides
  public SpireUserRolesClient provideSpireUserRolesClient(SpireClientConfig clientConfig) {
    SpireRequestConfig requestConfig =
        new SpireRequestConfig("SPIRE_USER_ROLES", "getRoles", true);
    SpireUserRolesErrorHandler errorHandler = new SpireUserRolesErrorHandler();
    return new SpireUserRolesClient(new SpireUserRolesParser(), clientConfig, requestConfig, errorHandler);
  }

  @Provides
  public SpireClientConfig provideSpireClientConfig(UserServiceConfiguration config) {
    return new SpireClientConfig(config.getSpireClientUsername(), config.getSpireClientPassword(), config.getSpireClientUrl());
  }
}
