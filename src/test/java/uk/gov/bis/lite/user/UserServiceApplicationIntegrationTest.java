package uk.gov.bis.lite.user;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.bis.lite.user.spire.SpireUserRolesUtil.stubForBody;

import org.junit.Test;
import uk.gov.bis.lite.user.api.view.CustomerView;
import uk.gov.bis.lite.user.api.view.Role;
import uk.gov.bis.lite.user.api.view.SiteView;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.integration.BaseIntegrationTest;

import javax.ws.rs.core.Response;

public class UserServiceApplicationIntegrationTest extends BaseIntegrationTest {

  private String urlTarget(String targetPath) {
    return "http://localhost:" + RULE.getLocalPort() + targetPath;
  }

  private Response get(String targetPath) {
    return RULE.client().target(urlTarget(targetPath))
        .request()
        .headers(authedHeaders)
        .get();
  }

  @Test
  public void singleCustomerTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSarAdmin.xml"));

    Response response = get("/user-privileges/123");

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    UserPrivilegesView userPrivs = response.readEntity(UserPrivilegesView.class);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void singleSiteTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSiteAdmin.xml"));

    Response response = get("/user-privileges/123");

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    UserPrivilegesView userPrivs = response.readEntity(UserPrivilegesView.class);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerAndSiteTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/SarAndSiteAdmin.xml"));

    Response response = get("/user-privileges/123");

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    UserPrivilegesView userPrivs = response.readEntity(UserPrivilegesView.class);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void junkRolesTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/JunkRole.xml"));

    Response response = get("/user-privileges/123");

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    UserPrivilegesView userPrivs = response.readEntity(UserPrivilegesView.class);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void noRolesTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/NoRoles.xml"));

    Response response = get("/user-privileges/123");

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    UserPrivilegesView userPrivs = response.readEntity(UserPrivilegesView.class);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void userIdDoesNotExistTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/UserIdDoesNotExist.xml"));

    Response response = get("/user-privileges/123");

    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void unhandledErrorTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/UnhandledError.xml"));

    Response response = get("/user-privileges/123");

    assertThat(response.getStatus()).isEqualTo(500);
  }

  @Test
  public void unauthorisedTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSarAdmin.xml"));

    Response response = RULE.client().target(urlTarget("/user-privileges/123")).request().get();

    String body = response.readEntity(String.class);

    assertThat(response.getStatus()).isEqualTo(401);
    assertThat(body).isEqualTo("Credentials are required to access this resource.");
  }

  @Test
  public void spireError500() throws Exception {
    stubFor(post(urlEqualTo("/spire/fox/ispire/SPIRE_USER_ROLES"))
        .willReturn(aResponse()
            .withStatus(500)));

    Response response = get("/user-privileges/123");

    assertThat(response.getStatus()).isEqualTo(500);
  }

  @Test
  public void userIdInRequestTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSarAdmin.xml"));

    get("/user-privileges/123");

    verify(postRequestedFor(urlEqualTo("/spire/fox/ispire/SPIRE_USER_ROLES"))
        .withRequestBody(matchingXPath("//SOAP-ENV:Envelope/SOAP-ENV:Body/spir:getRoles/userId[text() = '123']")
            .withXPathNamespace("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/")
            .withXPathNamespace("spir", "http://www.fivium.co.uk/fox/webservices/ispire/SPIRE_USER_ROLES")));
  }
}
