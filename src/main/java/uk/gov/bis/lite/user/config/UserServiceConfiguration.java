package uk.gov.bis.lite.user.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

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
  private String adminUsername;

  @NotEmpty
  @JsonProperty
  private String adminPassword;

  public String getSpireClientUsername() {
    return spireClientUsername;
  }

  public String getSpireClientPassword() {
    return spireClientPassword;
  }

  public String getSpireClientUrl() {
    return spireClientUrl;
  }

  public String getAdminUsername() {
    return adminUsername;
  }

  public String getAdminPassword() {
    return adminPassword;
  }
}
