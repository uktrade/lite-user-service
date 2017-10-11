package uk.gov.bis.lite.user.api.view;

import java.util.Collections;
import java.util.List;

public class UserPrivilegesView {
  private String userAccountType;
  private List<CustomerView> customers;
  private List<SiteView> sites;

  public UserPrivilegesView() {
    customers = Collections.emptyList();
    sites = Collections.emptyList();
  }

  public String getUserAccountType() {
    return userAccountType;
  }

  public UserPrivilegesView setUserAccountType(String userAccountType) {
    this.userAccountType = userAccountType;
    return this;
  }

  public List<CustomerView> getCustomers() {
    return customers;
  }

  public UserPrivilegesView setCustomers(List<CustomerView> customers) {
    this.customers = customers;
    return this;
  }

  public List<SiteView> getSites() {
    return sites;
  }

  public UserPrivilegesView setSites(List<SiteView> sites) {
    this.sites = sites;
    return this;
  }
}
