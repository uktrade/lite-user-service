package uk.gov.bis.lite.user.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;
import uk.gov.bis.lite.common.redis.RedisConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class UserServiceConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty
  private String spireClientUsername;

  @NotEmpty
  @JsonProperty
  private String spireClientPassword;

  @NotEmpty
  @JsonProperty
  private String spireClientUrl;

  @NotEmpty
  @JsonProperty
  private String jwtSharedSecret;

  @NotEmpty
  @JsonProperty
  private String adminLogin;

  @NotEmpty
  @JsonProperty
  private String adminPassword;

  @NotEmpty
  @JsonProperty
  private String serviceLogin;

  @NotEmpty
  @JsonProperty
  private String servicePassword;

  @NotNull
  @Valid
  @JsonProperty("redis")
  private RedisConfiguration redisConfiguration;

  public String getSpireClientUsername() {
    return spireClientUsername;
  }

  public String getSpireClientPassword() {
    return spireClientPassword;
  }

  public String getSpireClientUrl() {
    return spireClientUrl;
  }

  public String getJwtSharedSecret() {
    return jwtSharedSecret;
  }

  public String getAdminLogin() {
    return adminLogin;
  }

  public String getAdminPassword() {
    return adminPassword;
  }

  public String getServiceLogin() {
    return serviceLogin;
  }

  public String getServicePassword() {
    return servicePassword;
  }

  public RedisConfiguration getRedisConfiguration() {
    return redisConfiguration;
  }

}
