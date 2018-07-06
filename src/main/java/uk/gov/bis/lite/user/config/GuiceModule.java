package uk.gov.bis.lite.user.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import uk.gov.bis.lite.common.metrics.readiness.DefaultReadinessService;
import uk.gov.bis.lite.common.metrics.readiness.ReadinessService;
import uk.gov.bis.lite.common.spire.client.SpireClientConfig;
import uk.gov.bis.lite.common.spire.client.SpireRequestConfig;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.service.UserDetailsServiceImpl;
import uk.gov.bis.lite.user.service.UserPrivilegesService;
import uk.gov.bis.lite.user.service.UserPrivilegesServiceImpl;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsClient;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsErrorHandler;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsParser;
import uk.gov.bis.lite.user.spire.user.roles.SpireUserRolesClient;
import uk.gov.bis.lite.user.spire.user.roles.SpireUserRolesErrorHandler;
import uk.gov.bis.lite.user.spire.user.roles.SpireUserRolesParser;

public class GuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new RedisModule());
    bind(ReadinessService.class).to(DefaultReadinessService.class);
    bind(UserPrivilegesService.class).to(UserPrivilegesServiceImpl.class);
    bind(UserDetailsService.class).to(UserDetailsServiceImpl.class);
  }

  @Provides
  public SpireUserRolesClient provideSpireUserRolesClient(SpireClientConfig clientConfig) {
    SpireRequestConfig requestConfig =
        new SpireRequestConfig("SPIRE_USER_ROLES", "getRoles", true);
    SpireUserRolesErrorHandler errorHandler = new SpireUserRolesErrorHandler();
    return new SpireUserRolesClient(new SpireUserRolesParser(), clientConfig, requestConfig, errorHandler);
  }

  @Provides
  public SpireUserDetailsClient provideSpireUserDetailsClient(SpireClientConfig clientConfig) {
    SpireRequestConfig requestConfig =
        new SpireRequestConfig("SPIRE_USER_DETAILS", "USER_DETAILS", false);
    return new SpireUserDetailsClient(new SpireUserDetailsParser(), clientConfig, requestConfig, new SpireUserDetailsErrorHandler());
  }

  @Provides
  public SpireClientConfig provideSpireClientConfig(UserServiceConfiguration config) {
    return new SpireClientConfig(config.getSpireClientUsername(), config.getSpireClientPassword(), config.getSpireClientUrl());
  }
}
