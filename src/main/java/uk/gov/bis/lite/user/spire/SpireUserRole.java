package uk.gov.bis.lite.user.spire;

public class SpireUserRole {
  private String resType;
  private String roleName;
  private String fullName;
  private String sarRef;
  private String siteRef;
  private String isAdmin;
  private String isApplicant;

  private SpireUserRole(String resType, String roleName, String fullName, String sarRef, String siteRef, String isAdmin, String isApplicant) {
    this.resType = resType;
    this.roleName = roleName;
    this.fullName = fullName;
    this.sarRef = sarRef;
    this.siteRef = siteRef;
    this.isAdmin = isAdmin;
    this.isApplicant = isApplicant;
  }

  public String getResType() {
    return resType;
  }

  public String getRoleName() {
    return roleName;
  }

  public String getFullName() {
    return fullName;
  }

  public String getSarRef() {
    return sarRef;
  }

  public String getSiteRef() {
    return siteRef;
  }

  public String getIsAdmin() {
    return isAdmin;
  }

  public String getIsApplicant() {
    return isApplicant;
  }

  public static SpireUserRoleBuilder builder() {
    return new SpireUserRoleBuilder();
  }

  public static class SpireUserRoleBuilder {
    private String resType;
    private String roleName;
    private String fullName;
    private String sarRef;
    private String siteRef;
    private String isAdmin;
    private String isApplicant;

    public SpireUserRoleBuilder() {

    }

    public SpireUserRoleBuilder setResType(String resType) {
      this.resType = resType;
      return this;
    }

    public SpireUserRoleBuilder setRoleName(String roleName) {
      this.roleName = roleName;
      return this;
    }

    public SpireUserRoleBuilder setFullName(String fullName) {
      this.fullName = fullName;
      return this;
    }

    public SpireUserRoleBuilder setSarRef(String sarRef) {
      this.sarRef = sarRef;
      return this;
    }

    public SpireUserRoleBuilder setSiteRef(String siteRef) {
      this.siteRef = siteRef;
      return this;
    }

    public SpireUserRoleBuilder setIsAdmin(String isAdmin) {
      this.isAdmin = isAdmin;
      return this;
    }

    public SpireUserRoleBuilder setIsApplicant(String isApplicant) {
      this.isApplicant = isApplicant;
      return this;
    }

    public SpireUserRole build() {
      return new SpireUserRole(resType, roleName, fullName, sarRef, siteRef, isAdmin, isApplicant);
    }
  }
}
