package uk.gov.bis.lite.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomerView {
  private String customerId;
  private String role;

  @JsonCreator
  public CustomerView(@JsonProperty("customers") String customerId,
                      @JsonProperty("role") String role) {
    this.customerId = customerId;
    this.role = role;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getRole() {
    return role;
  }
}
