package uk.gov.bis.lite.user.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;


public class RedisConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty
  private boolean ssl;

  @NotEmpty
  @JsonProperty
  private String host;

  @NotEmpty
  @JsonProperty
  private int port;

  @JsonProperty
  private String password;

  @NotEmpty
  @JsonProperty
  private int database;

  @NotEmpty
  @JsonProperty
  private int timeout;

  @NotEmpty
  @JsonProperty
  private int poolMinIdle;

  @NotEmpty
  @JsonProperty
  private int poolMaxTotal;

  public boolean isSsl() {
    return ssl;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getPassword() {
    return password;
  }

  public int getDatabase() {
    return database;
  }

  public int getTimeout() {
    return timeout;
  }

  public int getPoolMinIdle() {
    return poolMinIdle;
  }

  public int getPoolMaxTotal() {
    return poolMaxTotal;
  }
}
