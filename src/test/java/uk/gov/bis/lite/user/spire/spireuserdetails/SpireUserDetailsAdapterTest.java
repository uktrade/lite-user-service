package uk.gov.bis.lite.user.spire.spireuserdetails;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import uk.gov.bis.lite.user.api.view.AccountStatus;
import uk.gov.bis.lite.user.api.view.AccountType;
import uk.gov.bis.lite.user.api.view.UserDetailsView;

public class SpireUserDetailsAdapterTest {
  private static final String TITLE = "mr";
  private static final String FIRST_NAME = "applicant1";
  private static final String LAST_NAME = "test";
  private static final String FULL_NAME = "mr applicant1 test";
  private static final String CONTACT_EMAIL_ADDRESS = "applicant1@test.com";
  private static final String CONTACT_PHONE_NUMBER = "+44 (0)208 123 4567";
  private static final String ACCOUNT_TYPE = "EXPORTER";
  private static final String ACCOUNT_STATUS = "ACTIVE";

  @Test
  public void adaptTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails()
        .setTitle(TITLE)
        .setFirstName(FIRST_NAME)
        .setLastName(LAST_NAME)
        .setFullName(FULL_NAME)
        .setContactEmailAddress(CONTACT_EMAIL_ADDRESS)
        .setContactPhoneNumber(CONTACT_PHONE_NUMBER)
        .setAccountType(ACCOUNT_TYPE)
        .setAccountStatus(ACCOUNT_STATUS);

    UserDetailsView userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getTitle()).isEqualTo(TITLE);
    assertThat(userDetailsView.getFirstName()).isEqualTo(FIRST_NAME);
    assertThat(userDetailsView.getLastName()).isEqualTo(LAST_NAME);
    assertThat(userDetailsView.getFullName()).isEqualTo(FULL_NAME);
    assertThat(userDetailsView.getContactEmailAddress()).isEqualTo(CONTACT_EMAIL_ADDRESS);
    assertThat(userDetailsView.getContactPhoneNumber()).isEqualTo(CONTACT_PHONE_NUMBER);
    assertThat(userDetailsView.getAccountType()).isEqualTo(AccountType.EXPORTER);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
  }

  @Test
  public void accountTypeTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountType("EXPORTER");
    UserDetailsView userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountType()).isEqualTo(AccountType.EXPORTER);

    spireUserDetails = new SpireUserDetails().setAccountType("REGULATOR");
    userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountType()).isEqualTo(AccountType.REGULATOR);

    spireUserDetails = new SpireUserDetails().setAccountType("CANCELLED");
    userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountType()).isEqualTo(AccountType.CANCELLED);

    spireUserDetails = new SpireUserDetails().setAccountType("SOMETHING ELSE");
    userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountType()).isNull();
  }

  @Test
  public void accountStatusTest() throws Exception {
    SpireUserDetails spireUserDetails = new SpireUserDetails().setAccountStatus("ACTIVE");
    UserDetailsView userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);

    spireUserDetails = new SpireUserDetails().setAccountStatus("BLOCKED");
    userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.BLOCKED);

    spireUserDetails = new SpireUserDetails().setAccountStatus("CANCELLED");
    userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.CANCELLED);

    spireUserDetails = new SpireUserDetails().setAccountStatus("NEW");
    userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.NEW);

    spireUserDetails = new SpireUserDetails().setAccountStatus("SUSPENDED");
    userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isEqualTo(AccountStatus.SUSPENDED);

    spireUserDetails = new SpireUserDetails().setAccountStatus("SOMETHING ELSE");
    userDetailsView = SpireUserDetailsAdapter.adapt(spireUserDetails);
    assertThat(userDetailsView.getAccountStatus()).isNull();
  }
}