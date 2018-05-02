package uk.gov.bis.lite.user.spire.spireuserdetails;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.bis.lite.user.api.view.UserAccountTypeView;
import uk.gov.bis.lite.user.api.view.UserDetailsView;
import uk.gov.bis.lite.user.api.view.enums.AccountStatus;
import uk.gov.bis.lite.user.api.view.enums.AccountType;

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

    UserDetailsView userDetailsView = SpireUserDetailsAdapter.mapToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getTitle()).isEqualTo(SpireUserDetailsTestUtils.TITLE);
    assertThat(userDetailsView.getFirstName()).isEqualTo(SpireUserDetailsTestUtils.FIRST_NAME);
    assertThat(userDetailsView.getLastName()).isEqualTo(SpireUserDetailsTestUtils.LAST_NAME);
    assertThat(userDetailsView.getFullName()).isEqualTo(SpireUserDetailsTestUtils.FULL_NAME);
    assertThat(userDetailsView.getContactEmailAddress()).isEqualTo(SpireUserDetailsTestUtils.CONTACT_EMAIL_ADDRESS);
    assertThat(userDetailsView.getContactPhoneNumber()).isEqualTo(SpireUserDetailsTestUtils.CONTACT_PHONE_NUMBER);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
  }

  @Test
  public void accountStatusActiveTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountStatus("ACTIVE");
    UserDetailsView userDetailsView = SpireUserDetailsAdapter.mapToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
  }

  @Test
  public void accountStatusBlockedTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountStatus("BLOCKED");
    UserDetailsView userDetailsView = SpireUserDetailsAdapter.mapToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.BLOCKED);
  }

  @Test
  public void accountStatusCancelledTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountStatus("CANCELLED");
    UserDetailsView userDetailsView = SpireUserDetailsAdapter.mapToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.CANCELLED);
  }

  @Test
  public void accountStatusNewTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountStatus("NEW");
    UserDetailsView userDetailsView = SpireUserDetailsAdapter.mapToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.NEW);
  }

  @Test
  public void accountStatusSuspendedTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountStatus("SUSPENDED");
    UserDetailsView userDetailsView = SpireUserDetailsAdapter.mapToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.SUSPENDED);
  }

  @Test
  public void accountStatusInvalidTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountStatus("SOMETHING ELSE");
    UserDetailsView userDetailsView = SpireUserDetailsAdapter.mapToUserDetailsView(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isNull();
  }

  @Test
  public void accountTypeExporterTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountType("EXPORTER");
    UserAccountTypeView userAccountTypeView = SpireUserDetailsAdapter.mapToUserAccountTypeView(spireUserDetails);
    assertThat(userAccountTypeView.getAccountType()).isEqualTo(AccountType.EXPORTER);
  }

  @Test
  public void accountTypeCancelledTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountType("CANCELLED");
    UserAccountTypeView userAccountTypeView = SpireUserDetailsAdapter.mapToUserAccountTypeView(spireUserDetails);
    assertThat(userAccountTypeView.getAccountType()).isEqualTo(AccountType.UNKNOWN);
  }

  @Test
  public void accountTypeGovernmentUserTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountType("GOVERNMENT_USER");
    UserAccountTypeView userAccountTypeView = SpireUserDetailsAdapter.mapToUserAccountTypeView(spireUserDetails);
    assertThat(userAccountTypeView.getAccountType()).isEqualTo(AccountType.REGULATOR);
  }

  @Test
  public void accountTypeInvalidTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountType("SOMETHING ELSE");
    UserAccountTypeView userAccountTypeView = SpireUserDetailsAdapter.mapToUserAccountTypeView(spireUserDetails);
    assertThat(userAccountTypeView.getAccountType()).isEqualTo(AccountType.UNKNOWN);
  }
}