package uk.gov.bis.lite.user.spire.spireuserdetails;

import org.apache.commons.lang3.StringUtils;
import uk.gov.bis.lite.user.api.view.AccountStatus;
import uk.gov.bis.lite.user.api.view.AccountType;
import uk.gov.bis.lite.user.api.view.UserAccountTypeView;
import uk.gov.bis.lite.user.api.view.UserDetailsView;

public class SpireUserDetailsAdapter {
  private SpireUserDetailsAdapter() {}

  public static UserDetailsView adaptToUserDetailsView(SpireUserDetails spireUserDetails) {
    return new UserDetailsView()
        .setTitle(spireUserDetails.getTitle())
        .setFirstName(spireUserDetails.getFirstName())
        .setLastName(spireUserDetails.getLastName())
        .setFullName(spireUserDetails.getFullName())
        .setContactEmailAddress(spireUserDetails.getContactEmailAddress())
        .setContactPhoneNumber(spireUserDetails.getContactPhoneNumber())
        .setAccountStatus(AccountStatus.getEnumByValue(spireUserDetails.getAccountStatus()).orElse(null));
  }

  public static UserAccountTypeView adaptToUserAccountTypeView(SpireUserDetails spireUserDetails) {
    return new UserAccountTypeView().setAccountType(accountTypeMapper(spireUserDetails.getAccountType()));
  }

  private static AccountType accountTypeMapper(String spireAccountType) {
    if (StringUtils.equals(spireAccountType, "GOVERNMENT_USER")) {
      return AccountType.REGULATOR;
    } else if (StringUtils.equals(spireAccountType, "EXPORTER")) {
      return AccountType.EXPORTER;
    } else {
      return AccountType.UNKNOWN;
    }
  }
}
