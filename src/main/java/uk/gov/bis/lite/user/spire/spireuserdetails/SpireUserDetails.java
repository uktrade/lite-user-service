package uk.gov.bis.lite.user.spire.spireuserdetails;

public class SpireUserDetails {
  private String title;
  private String firstName;
  private String lastName;
  private String fullName;
  private String contactEmailAddress;
  private String contactPhoneNumber;
  private String accountType;
  private String accountStatus;

  public String getTitle() {
    return title;
  }

  public SpireUserDetails setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public SpireUserDetails setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public SpireUserDetails setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public SpireUserDetails setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  public String getContactEmailAddress() {
    return contactEmailAddress;
  }

  public SpireUserDetails setContactEmailAddress(String contactEmailAddress) {
    this.contactEmailAddress = contactEmailAddress;
    return this;
  }

  public String getContactPhoneNumber() {
    return contactPhoneNumber;
  }

  public SpireUserDetails setContactPhoneNumber(String contactPhoneNumber) {
    this.contactPhoneNumber = contactPhoneNumber;
    return this;
  }

  public String getAccountType() {
    return accountType;
  }

  public SpireUserDetails setAccountType(String accountType) {
    this.accountType = accountType;
    return this;
  }

  public String getAccountStatus() {
    return accountStatus;
  }

  public SpireUserDetails setAccountStatus(String accountStatus) {
    this.accountStatus = accountStatus;
    return this;
  }
}
