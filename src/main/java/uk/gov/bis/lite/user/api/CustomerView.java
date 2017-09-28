package uk.gov.bis.lite.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerView {
  private String customerId;
  private Role role;

  @JsonCreator
  public CustomerView(@JsonProperty("customerId") String customerId,
                      @JsonProperty("role") Role role) {
    this.customerId = customerId;
    this.role = role;
  }

  public String getCustomerId() {
    return customerId;
  }

  public Role getRole() {
    return role;
  }
}
