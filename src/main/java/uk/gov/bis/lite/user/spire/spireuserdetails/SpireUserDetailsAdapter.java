package uk.gov.bis.lite.user.spire.spireuserdetails;

import uk.gov.bis.lite.user.api.view.AccountStatus;
import uk.gov.bis.lite.user.api.view.UserDetailsView;

public class SpireUserDetailsAdapter {
  private SpireUserDetailsAdapter() {}

  public static UserDetailsView adapt(SpireUserDetails spireUserDetails) {
    return new UserDetailsView()
        .setTitle(spireUserDetails.getTitle())
        .setFirstName(spireUserDetails.getFirstName())
        .setLastName(spireUserDetails.getLastName())
        .setFullName(spireUserDetails.getFullName())
        .setContactEmailAddress(spireUserDetails.getContactEmailAddress())
        .setContactPhoneNumber(spireUserDetails.getContactPhoneNumber())
        .setAccountStatus(AccountStatus.getEnumByValue(spireUserDetails.getAccountStatus()).orElse(null));
  }
}
