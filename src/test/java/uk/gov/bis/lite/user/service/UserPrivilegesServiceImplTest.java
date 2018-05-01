package uk.gov.bis.lite.user.service;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.buildCustomerAdmin;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.buildSiteAdmin;

import org.junit.Test;
import uk.gov.bis.lite.common.spire.client.SpireRequest;
import uk.gov.bis.lite.user.api.view.CustomerView;
import uk.gov.bis.lite.user.api.view.enums.Role;
import uk.gov.bis.lite.user.api.view.SiteView;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.spire.SpireUserNotFoundException;
import uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRoles;
import uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesClient;

import java.util.Arrays;
import java.util.Optional;

public class UserPrivilegesServiceImplTest {

  private SpireUserRolesClient client = mock(SpireUserRolesClient.class);

  private UserPrivilegesService service = new UserPrivilegesServiceImpl(client);

  @Test
  public void userHasCustomersAndSitesTest() throws Exception {
    when(client.createRequest()).thenReturn(mock(SpireRequest.class));
    when(client.sendRequest(any()))
        .thenReturn(
                new SpireUserRoles(Arrays.asList(
                  buildCustomerAdmin("SAR123"),
                  buildSiteAdmin("SITE123"))));

    Optional<UserPrivilegesView> userPrivsOpt = service.getUserPrivileges("123");
    assertThat(userPrivsOpt.isPresent()).isTrue();

    UserPrivilegesView userPrivs = userPrivsOpt.get();
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
    when(client.createRequest()).thenReturn(mock(SpireRequest.class));
    when(client.sendRequest(any()))
        .thenReturn(new SpireUserRoles(emptyList()));

    Optional<UserPrivilegesView> userPrivsOpt = service.getUserPrivileges("123");
    assertThat(userPrivsOpt.isPresent()).isTrue();

    UserPrivilegesView userPrivs = userPrivsOpt.get();
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void userDoesNotExistTest() throws Exception {
    when(client.createRequest()).thenReturn(mock(SpireRequest.class));
    when(client.sendRequest(any())).thenThrow(new SpireUserNotFoundException("User not found"));

    Optional<UserPrivilegesView> userPrivsOpt = service.getUserPrivileges("123");
    assertThat(userPrivsOpt.isPresent()).isFalse();
  }
}
