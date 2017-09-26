package uk.gov.bis.lite.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserPrivilegesView {
  private final String userAccountType;
  private final List<CustomerView> customers;
  private final List<SiteView> sites;

  @JsonCreator
  private UserPrivilegesView(@JsonProperty("userAccountType") String userAccountType,
                             @JsonProperty("customers") List<CustomerView> customers,
                             @JsonProperty("sites") List<SiteView> sites) {
    this.userAccountType = userAccountType;
    this.customers = customers;
    this.sites = sites;
  }

  public String getUserAccountType() {
    return userAccountType;
  }

  public List<CustomerView> getCustomers() {
    return customers;
  }

  public List<SiteView> getSites() {
    return sites;
  }

  public static class UserPrivilegesViewBuilder {
    private String userAccountType;
    private List<CustomerView> customers;
    private List<SiteView> sites;

    public UserPrivilegesViewBuilder() {
      userAccountType = "";
      customers = new ArrayList<>();
      sites = new ArrayList<>();
    }

    public UserPrivilegesViewBuilder setUserAccountType(String userAccountType) {
      this.userAccountType = userAccountType;
      return this;
    }

    public UserPrivilegesViewBuilder addCustomer(CustomerView customer) {
      customers.add(customer);
      return this;
    }

    public UserPrivilegesViewBuilder addCustomers(List<CustomerView> customers) {
      this.customers.addAll(customers);
      return this;
    }

    public UserPrivilegesViewBuilder addSite(SiteView site) {
      sites.add(site);
      return this;
    }

    public UserPrivilegesViewBuilder addSites(List<SiteView> sites) {
      this.sites.addAll(sites);
      return this;
    }

    public UserPrivilegesView build() {
      return new UserPrivilegesView(userAccountType, Collections.unmodifiableList(customers), Collections.unmodifiableList(sites));
    }
  }

  public static UserPrivilegesViewBuilder builder() {
    return new UserPrivilegesViewBuilder();
  }
}
