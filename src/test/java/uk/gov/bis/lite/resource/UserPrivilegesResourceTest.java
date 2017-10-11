package uk.gov.bis.lite.resource;

import static java.util.Arrays.asList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import uk.gov.bis.lite.user.api.view.CustomerView;
import uk.gov.bis.lite.user.api.view.Role;
import uk.gov.bis.lite.user.api.view.SiteView;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.resource.UserPrivilegesResource;
import uk.gov.bis.lite.user.service.UserPrivilegesService;

import java.util.Map;
import java.util.Optional;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class UserPrivilegesResourceTest {

  private static final String URL = "/user-privileges";

  private final UserPrivilegesService userPrivilegesService = mock(UserPrivilegesService.class);

  @Rule
  public final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new UserPrivilegesResource(userPrivilegesService))
      .build();

  @Test
  public void testUserPrivs() throws Exception {
    UserPrivilegesView userPrivs = new UserPrivilegesView()
        .setUserAccountType("APPLICANT")
        .setCustomers(asList(
            new CustomerView()
                .setCustomerId("CUSTOMER123")
                .setRole(Role.ADMIN)))
        .setSites(asList(
            new SiteView()
                .setSiteId("SITE123")
                .setRole(Role.PREPARER)));

    when(userPrivilegesService.getUserPrivileges("1")).thenReturn(Optional.of(userPrivs));

    Response response = resources.client()
        .target(URL + "/1")
        .request()
        .get();

    assertThat(response.getStatus()).isEqualTo(200);

    UserPrivilegesView result = response.readEntity(UserPrivilegesView.class);
    assertThat(result).isNotNull();
    assertThat(result.getUserAccountType()).isEqualTo("APPLICANT");
    assertThat(result.getCustomers().size()).isEqualTo(1);
    assertThat(result.getSites().size()).isEqualTo(1);

    CustomerView customer = result.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("CUSTOMER123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    SiteView site = result.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void testUserPrivsWithNoSitesOrCustomers() throws Exception {
    // User privs with only userAccountType set
    UserPrivilegesView userPrivs = new UserPrivilegesView()
        .setUserAccountType("APPLICANT");

    when(userPrivilegesService.getUserPrivileges("1")).thenReturn(Optional.of(userPrivs));

    Response response = resources.client()
        .target(URL + "/1")
        .request()
        .get();

    assertThat(response.getStatus()).isEqualTo(200);

    UserPrivilegesView result = response.readEntity(UserPrivilegesView.class);
    assertThat(result).isNotNull();
    assertThat(result.getUserAccountType()).isEqualTo("APPLICANT");
    assertThat(result.getCustomers().size()).isEqualTo(0);
    assertThat(result.getSites().size()).isEqualTo(0);
  }

  @Test
  public void testEmptyUserPrivs() throws Exception {
    // Empty user privs object
    UserPrivilegesView userPrivs = new UserPrivilegesView();

    when(userPrivilegesService.getUserPrivileges("1")).thenReturn(Optional.of(userPrivs));

    Response response = resources.client()
        .target(URL + "/1")
        .request()
        .get();

    assertThat(response.getStatus()).isEqualTo(200);

    UserPrivilegesView result = response.readEntity(UserPrivilegesView.class);
    assertThat(result).isNotNull();
    assertThat(result.getUserAccountType()).isNull();
    assertThat(result.getCustomers().size()).isEqualTo(0);
    assertThat(result.getSites().size()).isEqualTo(0);
  }

  @Test
  public void testUserPrivsNotFound() throws Exception {
    when(userPrivilegesService.getUserPrivileges("1")).thenReturn(Optional.empty());

    Response response = resources.client()
        .target(URL + "/1")
        .request()
        .get();

    assertThat(response.getStatus()).isEqualTo(404);

    Map<String, String> map = response.readEntity(new GenericType<Map<String, String>>(){});

    assertThat(map.entrySet().size()).isEqualTo(2);
    assertThat(map.get("code")).isEqualTo("404");
    assertThat(map.get("message")).isEqualTo("User not found.");
  }

}
