package uk.gov.bis.lite.user.api;

public class SiteView {
  private String siteId;
  private Role role;

  public SiteView() {}

  public String getSiteId() {
    return siteId;
  }

  public SiteView setSiteId(String siteId) {
    this.siteId = siteId;
    return this;
  }

  public Role getRole() {
    return role;
  }

  public SiteView setRole(Role role) {
    this.role = role;
    return this;
  }
}
