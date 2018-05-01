package uk.gov.bis.lite.user.resource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.bis.lite.user.TestUtils.generateBasicAuthorizationHeader;
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
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.assertj.core.api.Assertions;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.junit.Rule;
import org.junit.Test;
import uk.gov.bis.lite.common.auth.basic.SimpleAuthenticator;
import uk.gov.bis.lite.common.auth.basic.SimpleAuthorizer;
import uk.gov.bis.lite.common.auth.basic.User;
import uk.gov.bis.lite.user.TestUtils;
import uk.gov.bis.lite.user.api.view.enums.AccountType;
import uk.gov.bis.lite.user.api.view.UserAccountTypeView;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetails;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class UserAccountTypeResourceTest {
  private static final String ID = "24492";
  private static final String URL = "/user-account-type";
  private static final String ADMIN_USER = generateBasicAuthorizationHeader("admin","admin");
  private static final String SERVICE_USER = generateBasicAuthorizationHeader("service","service");
  private static final String UNKNOWN_USER = generateBasicAuthorizationHeader("user","user");

  private final UserDetailsService userDetailsService = mock(UserDetailsService.class);

  private final BasicCredentialAuthFilter<User> userBasicCredentialAuthFilter = new BasicCredentialAuthFilter.Builder<User>()
      .setAuthenticator(new SimpleAuthenticator("admin", "admin", "service", "service"))
      .setAuthorizer(new SimpleAuthorizer())
      .setRealm("User Service Authentication")
      .buildAuthFilter();

  @Rule
  public final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new UserAccountTypeResource(userDetailsService))
      .addProvider(new AuthDynamicFeature(userBasicCredentialAuthFilter))
      .addProvider(RolesAllowedDynamicFeature.class)
      .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
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

    for(String user: Arrays.asList(ADMIN_USER, SERVICE_USER)) {
      Response response = resources.client()
          .target(URL + "/" + ID)
          .request()
          .header(HttpHeaders.AUTHORIZATION, user)
          .get();

      assertThat(response.getStatus()).isEqualTo(200);

      UserAccountTypeView result = response.readEntity(UserAccountTypeView.class);
      assertThat(result.getAccountType()).isEqualTo(AccountType.EXPORTER);
    }
  }

  @Test
  public void userDoesNotExistTest() throws Exception {
    when(userDetailsService.getUserDetails(ID)).thenReturn(Optional.empty());

    for(String user: Arrays.asList(ADMIN_USER, SERVICE_USER)) {
      Response response = resources.client()
          .target(URL + "/" + ID)
          .request()
          .header(HttpHeaders.AUTHORIZATION, user)
          .get();

      assertThat(response.getStatus()).isEqualTo(404);

      Map<String, String> map = TestUtils.getMapFromResponse(response);
      Assertions.assertThat(map).hasSize(2);
      Assertions.assertThat(map).containsEntry("code", "404");
      Assertions.assertThat(map).containsEntry("message", "User not found");;
    }
  }

  @Test
  public void unauthorizedUser() throws Exception {
    when(userDetailsService.getUserDetails(ID)).thenReturn(Optional.empty());

    Response response = resources.client()
        .target(URL + "/" + ID)
        .request()
        .header(HttpHeaders.AUTHORIZATION, UNKNOWN_USER)
        .get();

    assertThat(response.getStatus()).isEqualTo(401);
  }

  @Test
  public void noAuthorizationHeader() throws Exception {
    when(userDetailsService.getUserDetails(ID)).thenReturn(Optional.empty());

    Response response = resources.client()
        .target(URL + "/" + ID)
        .request()
        .get();

    assertThat(response.getStatus()).isEqualTo(401);
  }
}