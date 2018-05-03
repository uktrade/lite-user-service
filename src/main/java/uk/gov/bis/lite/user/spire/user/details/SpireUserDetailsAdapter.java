package uk.gov.bis.lite.user.spire.user.details;

import uk.gov.bis.lite.user.api.view.UserAccountTypeView;
import uk.gov.bis.lite.user.api.view.UserDetailsView;
import uk.gov.bis.lite.user.api.view.enums.AccountStatus;
import uk.gov.bis.lite.user.api.view.enums.AccountType;
import uk.gov.bis.lite.user.util.EnumUtil;

public class SpireUserDetailsAdapter {
  private SpireUserDetailsAdapter() {}

  public static UserDetailsView mapToUserDetailsView(SpireUserDetails spireUserDetails) {
    return new UserDetailsView()
        .setTitle(spireUserDetails.getTitle())
        .setFirstName(spireUserDetails.getFirstName())
        .setLastName(spireUserDetails.getLastName())
        .setFullName(spireUserDetails.getFullName())
        .setContactEmailAddress(spireUserDetails.getContactEmailAddress())
        .setContactPhoneNumber(spireUserDetails.getContactPhoneNumber())
        .setAccountStatus(EnumUtil.parse(spireUserDetails.getAccountStatus(), AccountStatus.class));
  }

  public static UserAccountTypeView mapToUserAccountTypeView(SpireUserDetails spireUserDetails) {
    UserAccountTypeView userAccountTypeView = new UserAccountTypeView();
    AccountType accountType = mapToAccountType(spireUserDetails.getAccountType());
    userAccountTypeView.setAccountType(accountType);
    return userAccountTypeView;
  }

  private static AccountType mapToAccountType(String spireAccountType) {
    if ("GOVERNMENT_USER".equals(spireAccountType)) {
      return AccountType.REGULATOR;
    } else if ("EXPORTER".equals(spireAccountType)) {
      return AccountType.EXPORTER;
    } else {
      return AccountType.UNKNOWN;
    }
  }
}
