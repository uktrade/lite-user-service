package uk.gov.bis.lite.user.spire.user.roles;

import java.util.List;

public class SpireUserRoles {
  private final List<SpireUserRole> userRoles;

  public SpireUserRoles(List<SpireUserRole> userRoles) {
    this.userRoles = userRoles;
  }

  public List<SpireUserRole> getUserRoles() {
    return userRoles;
  }
}
