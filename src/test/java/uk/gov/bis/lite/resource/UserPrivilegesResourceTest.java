package uk.gov.bis.lite.resource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import uk.gov.bis.lite.user.api.CustomerView;
import uk.gov.bis.lite.user.api.Role;
import uk.gov.bis.lite.user.api.SiteView;
import uk.gov.bis.lite.user.api.UserPrivilegesView;
import uk.gov.bis.lite.user.resource.UserPrivilegesResource;
import uk.gov.bis.lite.user.service.UserPrivilegesService;

import java.util.Optional;

import javax.ws.rs.core.Response;

public class UserPrivilegesResourceTest {

  private static final String URL = "/user-privileges/user";

  private final UserPrivilegesService userPrivilegesService = mock(UserPrivilegesService.class);

  @Rule
  public final ResourceTestRule resources = ResourceTestRule.builder()
      .addResource(new UserPrivilegesResource(userPrivilegesService))
      .build();

  @Test
  public void testUserPrivs() throws Exception {
    UserPrivilegesView userPrivs = UserPrivilegesView.builder()
        .setUserAccountType("APPLICANT")
        .addCustomer(new CustomerView("CUSTOMER123", Role.ADMIN))
        .addSite(new SiteView("SITE123", Role.PREPARER))
        .build();

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
    UserPrivilegesView userPrivs = UserPrivilegesView.builder()
        .setUserAccountType("APPLICANT")
        .build();

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
    UserPrivilegesView userPrivs = UserPrivilegesView.builder().build();

    when(userPrivilegesService.getUserPrivileges("1")).thenReturn(Optional.of(userPrivs));

    Response response = resources.client()
        .target(URL + "/1")
        .request()
        .get();

    assertThat(response.getStatus()).isEqualTo(200);

    UserPrivilegesView result = response.readEntity(UserPrivilegesView.class);
    assertThat(result).isNotNull();
    assertThat(result.getUserAccountType()).isEmpty();
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
  }

}
