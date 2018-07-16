package uk.gov.bis.lite.user.config;

import com.google.inject.AbstractModule;
import ru.vyarus.dropwizard.guice.module.support.ConfigurationAwareModule;
import uk.gov.bis.lite.common.redis.RedisModule;
import uk.gov.bis.lite.user.service.RedisUserDetailsServiceImpl;
import uk.gov.bis.lite.user.service.RedisUserPrivilegesServiceImpl;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.service.UserPrivilegesService;

public class RedisServiceModule extends AbstractModule implements ConfigurationAwareModule<UserServiceConfiguration> {

  private UserServiceConfiguration userServiceConfiguration;

  @Override
  protected void configure() {
    install(new RedisModule(userServiceConfiguration.getRedisConfiguration()));
    bind(UserPrivilegesService.class).to(RedisUserPrivilegesServiceImpl.class);
    bind(UserDetailsService.class).to(RedisUserDetailsServiceImpl.class);
  }

  @Override
  public void setConfiguration(UserServiceConfiguration userServiceConfiguration) {
    this.userServiceConfiguration = userServiceConfiguration;
  }

}
