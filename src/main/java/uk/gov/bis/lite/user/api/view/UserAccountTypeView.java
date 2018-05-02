package uk.gov.bis.lite.user.api.view;

import uk.gov.bis.lite.user.api.view.enums.AccountType;

public class UserAccountTypeView {
  private AccountType accountType;

  public AccountType getAccountType() {
    return accountType;
  }

  public UserAccountTypeView setAccountType(AccountType accountType) {
    this.accountType = accountType;
    return this;
  }
}
