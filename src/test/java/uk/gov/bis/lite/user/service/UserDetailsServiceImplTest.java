package uk.gov.bis.lite.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsTestUtils.ACCOUNT_STATUS;
import static uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsTestUtils.ACCOUNT_TYPE;
import static uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsTestUtils.CONTACT_EMAIL_ADDRESS;
import static uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsTestUtils.CONTACT_PHONE_NUMBER;
import static uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsTestUtils.FIRST_NAME;
import static uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsTestUtils.FULL_NAME;
import static uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsTestUtils.LAST_NAME;
import static uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsTestUtils.TITLE;

import org.junit.Test;
import uk.gov.bis.lite.common.spire.client.SpireRequest;
import uk.gov.bis.lite.user.spire.SpireUserNotFoundException;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetails;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsClient;

import java.util.Optional;

public class UserDetailsServiceImplTest {

  private final SpireUserDetailsClient client = mock(SpireUserDetailsClient.class);

  private final UserDetailsService service = new UserDetailsServiceImpl(client);

  @Test
  public void userExistsTest() throws Exception {
    when(client.createRequest()).thenReturn(mock(SpireRequest.class));
    when(client.sendRequest(any()))
        .thenReturn(new SpireUserDetails()
            .setTitle(TITLE)
            .setFirstName(FIRST_NAME)
            .setLastName(LAST_NAME)
            .setFullName(FULL_NAME)
            .setContactEmailAddress(CONTACT_EMAIL_ADDRESS)
            .setContactPhoneNumber(CONTACT_PHONE_NUMBER)
            .setAccountType(ACCOUNT_TYPE)
            .setAccountStatus(ACCOUNT_STATUS));

    Optional<SpireUserDetails> userDetailsOpt = service.getUserDetails("1");
    assertThat(userDetailsOpt).isPresent();

    SpireUserDetails spireUserDetails = userDetailsOpt.get();

    assertThat(spireUserDetails.getTitle()).isEqualTo(TITLE);
    assertThat(spireUserDetails.getFirstName()).isEqualTo(FIRST_NAME);
    assertThat(spireUserDetails.getLastName()).isEqualTo(LAST_NAME);
    assertThat(spireUserDetails.getFullName()).isEqualTo(FULL_NAME);
    assertThat(spireUserDetails.getContactEmailAddress()).isEqualTo(CONTACT_EMAIL_ADDRESS);
    assertThat(spireUserDetails.getContactPhoneNumber()).isEqualTo(CONTACT_PHONE_NUMBER);
    assertThat(spireUserDetails.getAccountType()).isEqualTo(ACCOUNT_TYPE);
    assertThat(spireUserDetails.getAccountStatus()).isEqualTo(ACCOUNT_STATUS);
  }

  @Test
  public void userDoesNotExistTest() throws Exception {
    when(client.createRequest()).thenReturn(mock(SpireRequest.class));
    when(client.sendRequest(any())).thenThrow(new SpireUserNotFoundException("User not found!"));

    Optional<SpireUserDetails> userDetailsOpt = service.getUserDetails("1");
    assertThat(userDetailsOpt).isNotPresent();
  }
}