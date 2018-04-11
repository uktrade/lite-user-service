package uk.gov.bis.lite.user.spire.spireuserroles;

public class SpireUserRole {
  private String resType;
  private String roleName;
  private String fullName;
  private String sarRef;
  private String siteRef;
  private String isAdmin;
  private String isApplicant;

  public String getResType() {
    return resType;
  }

  public SpireUserRole setResType(String resType) {
    this.resType = resType;
    return this;
  }

  public String getRoleName() {
    return roleName;
  }

  public SpireUserRole setRoleName(String roleName) {
    this.roleName = roleName;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public SpireUserRole setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  public String getSarRef() {
    return sarRef;
  }

  public SpireUserRole setSarRef(String sarRef) {
    this.sarRef = sarRef;
    return this;
  }

  public String getSiteRef() {
    return siteRef;
  }

  public SpireUserRole setSiteRef(String siteRef) {
    this.siteRef = siteRef;
    return this;
  }

  public String getIsAdmin() {
    return isAdmin;
  }

  public SpireUserRole setIsAdmin(String isAdmin) {
    this.isAdmin = isAdmin;
    return this;
  }

  public String getIsApplicant() {
    return isApplicant;
  }

  public SpireUserRole setIsApplicant(String isApplicant) {
    this.isApplicant = isApplicant;
    return this;
  }
}
