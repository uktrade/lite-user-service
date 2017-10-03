package uk.gov.bis.lite.user.spire;

import java.util.List;

public class SpireUserRoles {
  private final String userAccountType;
  private final List<SpireUserRole> userRoles;

  public SpireUserRoles(String userAccountType, List<SpireUserRole> userRoles) {
    this.userAccountType = userAccountType;
    this.userRoles = userRoles;
  }

  public String getUserAccountType() {
    return userAccountType;
  }

  public List<SpireUserRole> getUserRoles() {
    return userRoles;
  }
}
