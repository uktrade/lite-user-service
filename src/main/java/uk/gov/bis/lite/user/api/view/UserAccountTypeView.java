package uk.gov.bis.lite.user.api.view;

public class UserAccountTypeView {
  private AccountType accountType;

  public UserAccountTypeView() {
  }

  public AccountType getAccountType() {
    return accountType;
  }

  public UserAccountTypeView setAccountType(AccountType accountType) {
    this.accountType = accountType;
    return this;
  }
}
