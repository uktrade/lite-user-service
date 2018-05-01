package uk.gov.bis.lite.user.spire.spireuserdetails;

import org.apache.commons.lang3.StringUtils;
import uk.gov.bis.lite.user.api.view.UserAccountTypeView;
import uk.gov.bis.lite.user.api.view.UserDetailsView;
import uk.gov.bis.lite.user.api.view.enums.AccountStatus;
import uk.gov.bis.lite.user.api.view.enums.AccountType;
import uk.gov.bis.lite.user.util.EnumUtil;

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
        .setAccountStatus(EnumUtil.parse(spireUserDetails.getAccountStatus(), AccountStatus.class));
  }

  public static UserAccountTypeView adaptToUserAccountTypeView(SpireUserDetails spireUserDetails) {
    UserAccountTypeView userAccountTypeView = new UserAccountTypeView();
    AccountType accountType = accountTypeMapper(spireUserDetails.getAccountType());
    userAccountTypeView.setAccountType(accountType);
    return userAccountTypeView;
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
