package uk.gov.bis.lite.user.api.view;

import uk.gov.bis.lite.user.api.view.enums.AccountStatus;

public class UserDetailsView {
  private String title;
  private String firstName;
  private String lastName;
  private String fullName;
  private String contactEmailAddress;
  private String contactPhoneNumber;
  private AccountStatus accountStatus;

  public String getTitle() {
    return title;
  }

  public UserDetailsView setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getFirstName() {
    return firstName;
  }

  public UserDetailsView setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public UserDetailsView setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getFullName() {
    return fullName;
  }

  public UserDetailsView setFullName(String fullName) {
    this.fullName = fullName;
    return this;
  }

  public String getContactEmailAddress() {
    return contactEmailAddress;
  }

  public UserDetailsView setContactEmailAddress(String contactEmailAddress) {
    this.contactEmailAddress = contactEmailAddress;
    return this;
  }

  public String getContactPhoneNumber() {
    return contactPhoneNumber;
  }

  public UserDetailsView setContactPhoneNumber(String contactPhoneNumber) {
    this.contactPhoneNumber = contactPhoneNumber;
    return this;
  }

  public AccountStatus getAccountStatus() {
    return accountStatus;
  }

  public UserDetailsView setAccountStatus(AccountStatus accountStatus) {
    this.accountStatus = accountStatus;
    return this;
  }
}
