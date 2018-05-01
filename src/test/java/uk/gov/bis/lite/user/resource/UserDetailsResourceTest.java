package uk.gov.bis.lite.user.resource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.bis.lite.user.TestUtils.generateJwtAuthorizationHeader;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.ACCOUNT_STATUS;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.ACCOUNT_TYPE;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.CONTACT_EMAIL_ADDRESS;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.CONTACT_PHONE_NUMBER;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.FIRST_NAME;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.FULL_NAME;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.LAST_NAME;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.TITLE;

import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import uk.gov.bis.lite.common.jwt.LiteJwtAuthFilterHelper;
import uk.gov.bis.lite.common.jwt.LiteJwtUser;
import uk.gov.bis.lite.user.TestUtils;
import uk.gov.bis.lite.user.api.view.enums.AccountStatus;
import uk.gov.bis.lite.user.api.view.UserDetailsView;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetails;

import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;


public class UserDetailsResourceTest {
  private static final String ID = "24492";
  private static final String URL = "/user-details";

  private final UserDetailsService userDetailsService = mock(UserDetailsService.class);

  @Rule
  public final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new UserDetailsResource(userDetailsService))
      .addProvider(new AuthDynamicFeature(LiteJwtAuthFilterHelper.buildAuthFilter(TestUtils.JWT_SHARED_SECRET)))
      .addProvider(new AuthValueFactoryProvider.Binder<>(LiteJwtUser.class))
      .build();

  @Test
  public void userExistsTest() throws Exception {
    SpireUserDetails userDetails = new SpireUserDetails()
        .setTitle(TITLE)
        .setFirstName(FIRST_NAME)
        .setLastName(LAST_NAME)
        .setFullName(FULL_NAME)
        .setContactEmailAddress(CONTACT_EMAIL_ADDRESS)
        .setContactPhoneNumber(CONTACT_PHONE_NUMBER)
        .setAccountType(ACCOUNT_TYPE)
        .setAccountStatus(ACCOUNT_STATUS);

    when(userDetailsService.getUserDetails(ID)).thenReturn(Optional.of(userDetails));

    Response response = resources.client()
        .target(URL + "/" + ID)
        .request()
        .header(HttpHeaders.AUTHORIZATION, generateJwtAuthorizationHeader(ID, CONTACT_EMAIL_ADDRESS, FULL_NAME))
        .get();

    assertThat(response.getStatus()).isEqualTo(200);

    UserDetailsView result = response.readEntity(UserDetailsView.class);
    assertThat(result.getTitle()).isEqualTo(TITLE);
    assertThat(result.getFirstName()).isEqualTo(FIRST_NAME);
    assertThat(result.getLastName()).isEqualTo(LAST_NAME);
    assertThat(result.getFullName()).isEqualTo(FULL_NAME);
    assertThat(result.getContactEmailAddress()).isEqualTo(CONTACT_EMAIL_ADDRESS);
    assertThat(result.getContactPhoneNumber()).isEqualTo(CONTACT_PHONE_NUMBER);
    assertThat(result.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
  }

  @Test
  public void userDoesNotExistTest() throws Exception {
    when(userDetailsService.getUserDetails(ID)).thenReturn(Optional.empty());

    Response response = resources.client()
        .target(URL + "/" + ID)
        .request()
        .header(HttpHeaders.AUTHORIZATION, generateJwtAuthorizationHeader(ID, CONTACT_EMAIL_ADDRESS, FULL_NAME))
        .get();

    assertThat(response.getStatus()).isEqualTo(404);

    Map<String, String> map = TestUtils.getMapFromResponse(response);
    Assertions.assertThat(map).hasSize(2);
    Assertions.assertThat(map).containsEntry("code", "404");
    Assertions.assertThat(map).containsEntry("message", "User not found");
  }

  @Test
  public void userIdTokenDoesNotMatchUrlTest() throws Exception {
    Response response = resources.client()
        .target(URL + "/" + "1")
        .request()
        .header(HttpHeaders.AUTHORIZATION, generateJwtAuthorizationHeader("999", CONTACT_EMAIL_ADDRESS, FULL_NAME))
        .get();

    assertThat(response.getStatus()).isEqualTo(401);

    Map<String, String> map = TestUtils.getMapFromResponse(response);
    Assertions.assertThat(map).hasSize(2);
    Assertions.assertThat(map).containsEntry("code", "401");
    Assertions.assertThat(map).containsEntry("message", "userId 1 does not match value supplied in token 999");
  }

  @Test
  public void tokenNotSuppliedTest() throws Exception {
    Response response = resources.client()
        .target(URL + "/" + ID)
        .request()
        .get();

    assertThat(response.getStatus()).isEqualTo(401);
  }
}