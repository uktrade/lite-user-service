package uk.gov.bis.lite;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.stubForBody;

import org.junit.Test;
import uk.gov.bis.lite.integration.BaseIntegrationTest;
import uk.gov.bis.lite.user.api.CustomerView;
import uk.gov.bis.lite.user.api.Role;
import uk.gov.bis.lite.user.api.SiteView;
import uk.gov.bis.lite.user.api.UserPrivilegesView;

import javax.ws.rs.core.Response;

public class UserServiceApplicationIntegrationTest extends BaseIntegrationTest {

  public String urlTarget(String targetPath) {
    return "http://localhost:" + RULE.getLocalPort() + targetPath;
  }

  @Test
  public void singleCustomerTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSarAdmin.xml"));

    Response response = RULE.client().target(urlTarget("/user-privileges/user/123")).request().get();

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

    Response response = RULE.client().target(urlTarget("/user-privileges/user/123")).request().get();

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

    Response response = RULE.client().target(urlTarget("/user-privileges/user/123")).request().get();

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

    Response response = RULE.client().target(urlTarget("/user-privileges/user/123")).request().get();

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    UserPrivilegesView userPrivs = response.readEntity(UserPrivilegesView.class);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void noRolesTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/NoRoles.xml"));

    Response response = RULE.client().target(urlTarget("/user-privileges/user/123")).request().get();

    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/json");

    UserPrivilegesView userPrivs = response.readEntity(UserPrivilegesView.class);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void userIdDoesNotExistTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/UserIdDoesNotExist.xml"));

    Response response = RULE.client().target(urlTarget("/user-privileges/user/123")).request().get();

    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void unhandledErrorTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/UnhandledError.xml"));

    Response response = RULE.client().target(urlTarget("/user-privileges/user/123")).request().get();

    assertThat(response.getStatus()).isEqualTo(400);
  }

}
