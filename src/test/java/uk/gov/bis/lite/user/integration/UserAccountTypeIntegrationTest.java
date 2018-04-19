package uk.gov.bis.lite.user.integration;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.bis.lite.user.TestUtils.generateBasicAuthorizationHeader;
import static uk.gov.bis.lite.user.TestUtils.getMapFromResponse;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.stubForBody;

import org.junit.Test;
import uk.gov.bis.lite.user.api.view.AccountType;
import uk.gov.bis.lite.user.api.view.UserAccountTypeView;

import java.util.Arrays;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

public class UserAccountTypeIntegrationTest extends BaseIntegrationTest {
  private static final String ID = "24492";
  private static final String URL = "/user-account-type";
  private static final String ADMIN_USER = generateBasicAuthorizationHeader("admin","admin");
  private static final String SERVICE_USER = generateBasicAuthorizationHeader("service","service");
  private static final String UNKNOWN_USER = generateBasicAuthorizationHeader("user","user");

  private String urlTarget(String targetPath) {
    return "http://localhost:" + RULE.getLocalPort() + targetPath;
  }

  private Response get(String targetPath, String authorizationHeader) {
    return RULE.client().target(urlTarget(targetPath))
        .request()
        .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
        .get();
  }

  @Test
  public void emptyUserDetailsTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/EmptyUserDetails.xml"));

    for(String user : Arrays .asList(ADMIN_USER, SERVICE_USER)) {
      Response response = get(URL + "/" + ID, user);

      assertThat(response.getStatus()).isEqualTo(200);
      assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

      UserAccountTypeView userAccountType = response.readEntity(UserAccountTypeView.class);
      assertThat(userAccountType.getAccountType()).isEqualTo(AccountType.UNKNOWN);
    }
  }

  @Test
  public void emptyUserDetailsListTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/EmptyUserDetailsList.xml"));

    for(String user : Arrays .asList(ADMIN_USER, SERVICE_USER)) {
      Response response = get(URL + "/" + ID, user);

      assertThat(response.getStatus()).isEqualTo(500);
      assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

      Map<String, String> responseMap = getMapFromResponse(response);
      assertThat(responseMap).hasSize(2);
      assertThat(responseMap.get("code")).isEqualTo("500");
      assertThat(responseMap.get("message")).isEqualTo("Unexpected number of USER_DETAILS found while parsing the SOAP response body, expected 1 but got 0");
    }
  }

  @Test
  public void MultipleUserDetailsTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/MultipleUserDetails.xml"));

    for(String user : Arrays .asList(ADMIN_USER, SERVICE_USER)) {
      Response response = get(URL + "/" + ID, user);

      assertThat(response.getStatus()).isEqualTo(500);
      assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

      Map<String, String> responseMap = getMapFromResponse(response);
      assertThat(responseMap).hasSize(2);
      assertThat(responseMap.get("code")).isEqualTo("500");
      assertThat(responseMap.get("message")).isEqualTo("Unexpected number of USER_DETAILS found while parsing the SOAP response body, expected 1 but got 2");
    }
  }

  @Test
  public void unhandledErrorTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/UnhandledError.xml"));

    for(String user : Arrays .asList(ADMIN_USER, SERVICE_USER)) {
      Response response = get(URL + "/" + ID, user);

      assertThat(response.getStatus()).isEqualTo(500);
      assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

      Map<String, String> responseMap = getMapFromResponse(response);
      assertThat(responseMap).hasSize(2);
      assertThat(responseMap.get("code")).isEqualTo("500");
      assertThat(responseMap.get("message")).startsWith("There was an error processing your request");
    }
  }

  @Test
  public void UserIdDoesNotExistTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/UserIdDoesNotExist.xml"));

    for(String user : Arrays .asList(ADMIN_USER, SERVICE_USER)) {
      Response response = get(URL + "/" + ID, user);

      assertThat(response.getStatus()).isEqualTo(404);
      assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

      Map<String, String> responseMap = getMapFromResponse(response);
      assertThat(responseMap).hasSize(2);
      assertThat(responseMap.get("code")).isEqualTo("404");
      assertThat(responseMap.get("message")).isEqualTo("User not found");
    }
  }

  @Test
  public void validUserDetailsTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/ValidUserDetails.xml"));

    for(String user : Arrays .asList(ADMIN_USER, SERVICE_USER)) {
      Response response = get(URL + "/" + ID, user);

      assertThat(response.getStatus()).isEqualTo(200);
      assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

      UserAccountTypeView userAccountType = response.readEntity(UserAccountTypeView.class);
      assertThat(userAccountType.getAccountType()).isEqualTo(AccountType.EXPORTER);
    }
  }
}
