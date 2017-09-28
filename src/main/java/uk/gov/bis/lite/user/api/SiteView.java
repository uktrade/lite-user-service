package uk.gov.bis.lite.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SiteView {
  private final String siteId;
  private final Role role;

  @JsonCreator
  public SiteView(@JsonProperty("siteId") String siteId,
                  @JsonProperty("role") Role role) {
    this.siteId = siteId;
    this.role = role;
  }

  public String getSiteId() {
    return siteId;
  }

  public Role getRole() {
    return role;
  }
}
