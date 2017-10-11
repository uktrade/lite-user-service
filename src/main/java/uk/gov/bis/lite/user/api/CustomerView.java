package uk.gov.bis.lite.user.api;

public class CustomerView {
  private String customerId;
  private Role role;

  public CustomerView() {}

  public String getCustomerId() {
    return customerId;
  }

  public CustomerView setCustomerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  public Role getRole() {
    return role;
  }

  public CustomerView setRole(Role role) {
    this.role = role;
    return this;
  }
}
