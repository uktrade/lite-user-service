package uk.gov.bis.lite.service;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.buildCustomerAdmin;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.buildSiteAdmin;

import org.junit.Test;
import uk.gov.bis.lite.user.api.view.CustomerView;
import uk.gov.bis.lite.user.api.view.Role;
import uk.gov.bis.lite.user.api.view.SiteView;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.service.UserPrivilegesService;
import uk.gov.bis.lite.user.service.UserPrivilegesServiceImpl;
import uk.gov.bis.lite.user.spire.SpireUserRoles;
import uk.gov.bis.lite.user.spire.SpireUserRolesClient;

import java.util.Arrays;
import java.util.Optional;

public class UserPrivilegesServiceImplTest {

  private static final String USER_ACCOUNT_TYPE = "REGULATOR";

  private SpireUserRolesClient client = mock(SpireUserRolesClient.class);

  private UserPrivilegesService service = new UserPrivilegesServiceImpl(client);

  @Test
  public void userHasCustomersAndSitesTest() throws Exception {
    when(client.sendRequest("123"))
        .thenReturn(
            Optional.of(
                new SpireUserRoles("REGULATOR", Arrays.asList(
                  buildCustomerAdmin("SAR123"),
                  buildSiteAdmin("SITE123")))));

    Optional<UserPrivilegesView> userPrivsOpt = service.getUserPrivileges("123");
    assertThat(userPrivsOpt.isPresent()).isTrue();

    UserPrivilegesView userPrivs = userPrivsOpt.get();
    assertThat(userPrivs.getUserAccountType()).isEqualTo(USER_ACCOUNT_TYPE);
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
  public void userHasNoCustomersOrSitesTest() throws Exception {
    when(client.sendRequest("123"))
        .thenReturn(Optional.of(new SpireUserRoles("REGULATOR", emptyList())));

    Optional<UserPrivilegesView> userPrivsOpt = service.getUserPrivileges("123");
    assertThat(userPrivsOpt.isPresent()).isTrue();

    UserPrivilegesView userPrivs = userPrivsOpt.get();
    assertThat(userPrivs.getUserAccountType()).isEqualTo(USER_ACCOUNT_TYPE);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void userHasNoData() throws Exception {
    when(client.sendRequest("123"))
        .thenReturn(Optional.of(new SpireUserRoles("", emptyList())));

    Optional<UserPrivilegesView> userPrivsOpt = service.getUserPrivileges("123");
    assertThat(userPrivsOpt.isPresent()).isTrue();

    UserPrivilegesView userPrivs = userPrivsOpt.get();
    assertThat(userPrivs.getUserAccountType()).isEmpty();
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void userDoesNotExist() throws Exception {
    when(client.sendRequest("123")).thenReturn(Optional.empty());

    Optional<UserPrivilegesView> userPrivsOpt = service.getUserPrivileges("123");
    assertThat(userPrivsOpt.isPresent()).isFalse();
  }
}
