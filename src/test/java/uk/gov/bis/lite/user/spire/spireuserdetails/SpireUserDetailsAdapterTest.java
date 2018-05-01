package uk.gov.bis.lite.user.spire.spireuserdetails;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.bis.lite.user.api.view.enums.AccountStatus;
import uk.gov.bis.lite.user.api.view.enums.AccountType;
import uk.gov.bis.lite.user.api.view.UserAccountTypeView;
import uk.gov.bis.lite.user.api.view.UserDetailsView;

public class SpireUserDetailsAdapterTest {

  @Test
  public void adaptTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails()
        .setTitle(SpireUserDetailsTestUtils.TITLE)
        .setFirstName(SpireUserDetailsTestUtils.FIRST_NAME)
        .setLastName(SpireUserDetailsTestUtils.LAST_NAME)
        .setFullName(SpireUserDetailsTestUtils.FULL_NAME)
        .setContactEmailAddress(SpireUserDetailsTestUtils.CONTACT_EMAIL_ADDRESS)
        .setContactPhoneNumber(SpireUserDetailsTestUtils.CONTACT_PHONE_NUMBER)
        .setAccountType(SpireUserDetailsTestUtils.ACCOUNT_TYPE)
        .setAccountStatus(SpireUserDetailsTestUtils.ACCOUNT_STATUS);

    UserDetailsView userDetailsView = SpireUserDetailsAdapter.adaptToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getTitle()).isEqualTo(SpireUserDetailsTestUtils.TITLE);
    assertThat(userDetailsView.getFirstName()).isEqualTo(SpireUserDetailsTestUtils.FIRST_NAME);
    assertThat(userDetailsView.getLastName()).isEqualTo(SpireUserDetailsTestUtils.LAST_NAME);
    assertThat(userDetailsView.getFullName()).isEqualTo(SpireUserDetailsTestUtils.FULL_NAME);
    assertThat(userDetailsView.getContactEmailAddress()).isEqualTo(SpireUserDetailsTestUtils.CONTACT_EMAIL_ADDRESS);
    assertThat(userDetailsView.getContactPhoneNumber()).isEqualTo(SpireUserDetailsTestUtils.CONTACT_PHONE_NUMBER);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
  }

  @Test
  public void accountStatusTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountStatus("ACTIVE");
    UserDetailsView userDetailsView = SpireUserDetailsAdapter.adaptToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);

    spireUserDetails = new SpireUserDetails().setAccountStatus("BLOCKED");
    userDetailsView = SpireUserDetailsAdapter.adaptToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.BLOCKED);

    spireUserDetails = new SpireUserDetails().setAccountStatus("CANCELLED");
    userDetailsView = SpireUserDetailsAdapter.adaptToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.CANCELLED);

    spireUserDetails = new SpireUserDetails().setAccountStatus("NEW");
    userDetailsView = SpireUserDetailsAdapter.adaptToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.NEW);

    spireUserDetails = new SpireUserDetails().setAccountStatus("SUSPENDED");
    userDetailsView = SpireUserDetailsAdapter.adaptToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.SUSPENDED);

    spireUserDetails = new SpireUserDetails().setAccountStatus("SOMETHING ELSE");
    userDetailsView = SpireUserDetailsAdapter.adaptToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isNull();
  }

  @Test
  public void accountTypeTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountType("EXPORTER");
    UserAccountTypeView userAccountTypeView = SpireUserDetailsAdapter.adaptToUserAccountTypeView(spireUserDetails);
    assertThat(userAccountTypeView.getAccountType()).isEqualTo(AccountType.EXPORTER);

    spireUserDetails = new SpireUserDetails().setAccountType("CANCELLED");
    userAccountTypeView = SpireUserDetailsAdapter.adaptToUserAccountTypeView(spireUserDetails);
    assertThat(userAccountTypeView.getAccountType()).isEqualTo(AccountType.UNKNOWN);

    spireUserDetails = new SpireUserDetails().setAccountType("GOVERNMENT_USER");
    userAccountTypeView = SpireUserDetailsAdapter.adaptToUserAccountTypeView(spireUserDetails);
    assertThat(userAccountTypeView.getAccountType()).isEqualTo(AccountType.REGULATOR);

    spireUserDetails = new SpireUserDetails().setAccountType("SOMETHING ELSE");
    userAccountTypeView = SpireUserDetailsAdapter.adaptToUserAccountTypeView(spireUserDetails);
    assertThat(userAccountTypeView.getAccountType()).isEqualTo(AccountType.UNKNOWN);
  }
}