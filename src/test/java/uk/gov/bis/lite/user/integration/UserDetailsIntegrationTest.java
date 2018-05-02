package uk.gov.bis.lite.user.integration;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.bis.lite.user.TestUtils.generateJwtAuthorizationHeader;
import static uk.gov.bis.lite.user.TestUtils.getMapFromResponse;
import static uk.gov.bis.lite.user.spire.spireuserdetails.SpireUserDetailsTestUtils.stubForBody;

import org.junit.Test;
import uk.gov.bis.lite.user.api.view.UserDetailsView;
import uk.gov.bis.lite.user.api.view.enums.AccountStatus;

import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class UserDetailsIntegrationTest extends BaseIntegrationTest {

  private final MultivaluedMap<String, Object> authedHeaders;

  public UserDetailsIntegrationTest() {
    authedHeaders = new MultivaluedHashMap<>();
    authedHeaders.put(HttpHeaders.AUTHORIZATION, Collections.singletonList(generateJwtAuthorizationHeader("24492", "test@example.org", "Mr Test")));
  }

  private Response get(String targetPath) {
    return RULE.client().target(urlTarget(targetPath, RULE.getLocalPort()))
        .request()
        .headers(authedHeaders)
        .get();
  }

  @Test
  public void emptyUserDetailsTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/EmptyUserDetails.xml"));

    Response response = get("/user-details/24492");

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    UserDetailsView userDetails = response.readEntity(UserDetailsView.class);
    assertThat(userDetails.getTitle()).isNull();
    assertThat(userDetails.getFirstName()).isNull();
    assertThat(userDetails.getLastName()).isNull();
    assertThat(userDetails.getFullName()).isNull();
    assertThat(userDetails.getContactEmailAddress()).isNull();
    assertThat(userDetails.getContactPhoneNumber()).isNull();
    assertThat(userDetails.getAccountStatus()).isNull();
  }

  @Test
  public void emptyUserDetailsListTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/EmptyUserDetailsList.xml"));

    Response response = get("/user-details/24492");

    assertThat(response.getStatus()).isEqualTo(500);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    Map<String, String> responseMap = getMapFromResponse(response);
    assertThat(responseMap).hasSize(2);
    assertThat(responseMap.get("code")).isEqualTo("500");
  }

  @Test
  public void multipleUserDetailsTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/MultipleUserDetails.xml"));

    Response response = get("/user-details/24492");

    assertThat(response.getStatus()).isEqualTo(500);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    Map<String, String> responseMap = getMapFromResponse(response);
    assertThat(responseMap).hasSize(2);
    assertThat(responseMap.get("code")).isEqualTo("500");
  }


  @Test
  public void unhandledErrorTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/UnhandledError.xml"));

    Response response = get("/user-details/24492");

    assertThat(response.getStatus()).isEqualTo(500);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    Map<String, String> responseMap = getMapFromResponse(response);
    assertThat(responseMap).hasSize(2);
    assertThat(responseMap.get("code")).isEqualTo("500");
    assertThat(responseMap.get("message")).startsWith("There was an error processing your request");
  }

  @Test
  public void UserIdDoesNotExistTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/UserIdDoesNotExist.xml"));

    Response response = get("/user-details/24492");

    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    Map<String, String> responseMap = getMapFromResponse(response);
    assertThat(responseMap).hasSize(2);
    assertThat(responseMap.get("code")).isEqualTo("404");
    assertThat(responseMap.get("message")).isEqualTo("User not found");
  }

  @Test
  public void validUserDetailsTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_DETAILS/ValidUserDetails.xml"));

    Response response = get("/user-details/24492");

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    UserDetailsView userDetails = response.readEntity(UserDetailsView.class);
    assertThat(userDetails.getTitle()).isEqualTo("mr");
    assertThat(userDetails.getFirstName()).isEqualTo("applicant1");
    assertThat(userDetails.getLastName()).isEqualTo("test");
    assertThat(userDetails.getFullName()).isEqualTo("mr applicant1 test");
    assertThat(userDetails.getContactEmailAddress()).isEqualTo("applicant1@test.com");
    assertThat(userDetails.getContactPhoneNumber()).isEqualTo("+44 (0)208 123 4567");
    assertThat(userDetails.getAccountStatus()).isEqualTo(AccountStatus.ACTIVE);
  }
}
