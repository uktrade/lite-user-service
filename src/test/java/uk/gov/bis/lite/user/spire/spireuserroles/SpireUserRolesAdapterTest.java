package uk.gov.bis.lite.user.spire.spireuserroles;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.FULL_NAME;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.RES_TYPE_SPIRE_SAR_USERS;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.RES_TYPE_SPIRE_SITE_USERS;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.ROLE_SAR_ADMINISTRATOR;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.ROLE_SITE_ADMINISTRATOR;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.buildCustomerAdmin;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.buildCustomerPreparer;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.buildCustomerSubmitter;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.buildSiteAdmin;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.buildSitePreparer;
import static uk.gov.bis.lite.user.spire.spireuserroles.SpireUserRolesTestUtils.buildSiteSubmitter;

import org.junit.Test;
import uk.gov.bis.lite.user.api.view.CustomerView;
import uk.gov.bis.lite.user.api.view.enums.Role;
import uk.gov.bis.lite.user.api.view.SiteView;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;

import java.util.Arrays;
import java.util.Optional;

public class SpireUserRolesAdapterTest {

  private static SpireUserRoles buildSpireUserRoles(SpireUserRole... userRole) {
    return new SpireUserRoles(Arrays.asList(userRole));
  }

  @Test
  public void customerIsAdminTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerIsSubmitterTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildCustomerSubmitter("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerIsPreparerTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void siteIsAdminTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildSiteAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SAR123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteIsSubmitterTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildSiteSubmitter("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteIsPreparerTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void customerAndSiteTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(buildCustomerAdmin("SAR123"), buildSiteAdmin("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void multipleCustomersAndSitesTest() throws Exception {
    // Tests ordering of customers and sites list
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerAdmin("SAR123"),
        buildCustomerAdmin("SAR456"),
        buildSiteAdmin("SITE123"),
        buildSiteAdmin("SITE456"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(2);
    assertThat(userPrivs.getSites().size()).isEqualTo(2);

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    customer = userPrivs.getCustomers().get(1);
    assertThat(customer.getCustomerId()).isEqualTo("SAR456");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);

    site = userPrivs.getSites().get(1);
    assertThat(site.getSiteId()).isEqualTo("SITE456");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void multipleCustomersAndSitesReversedTest() throws Exception {
    // Tests ordering of customers and sites list
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteAdmin("SITE456"),
        buildSiteAdmin("SITE123"),
        buildCustomerAdmin("SAR456"),
        buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(2);
    assertThat(userPrivs.getSites().size()).isEqualTo(2);

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    customer = userPrivs.getCustomers().get(1);
    assertThat(customer.getCustomerId()).isEqualTo("SAR456");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);

    site = userPrivs.getSites().get(1);
    assertThat(site.getSiteId()).isEqualTo("SITE456");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerRoleAdminPriorityTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerAdmin("SAR123"),
        buildCustomerSubmitter("SAR123"),
        buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerRoleAdminPriorityReversedTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerPreparer("SAR123"),
        buildCustomerSubmitter("SAR123"),
        buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteRoleAdminPriorityTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteAdmin("SITE123"),
        buildSiteSubmitter("SITE123"),
        buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteRoleAdminPriorityReversedTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSitePreparer("SITE123"),
        buildSiteSubmitter("SITE123"),
        buildSiteAdmin("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerRoleSubmitterPriorityTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerSubmitter("SAR123"),
        buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerRoleSubmitterPriorityReversedTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerPreparer("SAR123"),
        buildCustomerSubmitter("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteRoleSubmitterPriorityTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteSubmitter("SITE123"),
        buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteRoleAdminSubmitterReversedTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSitePreparer("SITE123"),
        buildSiteSubmitter("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void ignoreDuplicateRolesTest() throws Exception {
    // Admin -> Submitter -> Preparer
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerAdmin("SAR123"),
        buildCustomerAdmin("SAR123"),
        buildCustomerSubmitter("SAR123"),
        buildCustomerSubmitter("SAR123"),
        buildCustomerPreparer("SAR123"),
        buildCustomerPreparer("SAR123"),
        buildSiteAdmin("SITE123"),
        buildSiteAdmin("SITE123"),
        buildSiteSubmitter("SITE123"),
        buildSiteSubmitter("SITE123"),
        buildSitePreparer("SITE123"),
        buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
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
  public void ignoreUnusedOrEmptyRolesTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        new SpireUserRole()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName("SOME_OTHER_ROLE")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME),
        new SpireUserRole()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName("")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME),
        new SpireUserRole()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName(null)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME),
        new SpireUserRole()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName("SOME_OTHER_ROLE")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME),
        new SpireUserRole()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName("")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME),
        new SpireUserRole()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName(null)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
        );

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void ignoreEmptySarRefs() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildCustomerAdmin(""),
        buildCustomerAdmin(null));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void ignoreEmptySiteRefs() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        buildSiteAdmin(""),
        buildSiteAdmin(null));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void ignoreEmptyResType() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        new SpireUserRole()
            .setResType("")
            .setSarRef("SAR123")
            .setRoleName(ROLE_SAR_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME),
        new SpireUserRole()
            .setResType("")
            .setSiteRef("SITE123")
            .setRoleName(ROLE_SITE_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME),
        new SpireUserRole()
            .setResType(null)
            .setSarRef("SAR123")
            .setRoleName(ROLE_SAR_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME),
        new SpireUserRole()
            .setResType(null)
            .setSiteRef("SITE123")
            .setRoleName(ROLE_SITE_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
    );

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void tolerantOfUnusedFieldTest() throws Exception {
    SpireUserRoles sur = buildSpireUserRoles(
        new SpireUserRole()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName(ROLE_SAR_ADMINISTRATOR),
        new SpireUserRole()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName(ROLE_SITE_ADMINISTRATOR)
        );

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(sur);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
  }

  @Test
  public void sarAdminRoleMappingTest() throws Exception {
      Optional<Role> role = SpireUserRolesAdapter.mapSpireRole("SAR_ADMINISTRATOR");
      assertThat(role).isPresent().contains(Role.ADMIN);
  }

  @Test
  public void siteAdminRoleMappingTest() throws Exception {
    Optional<Role> role = SpireUserRolesAdapter.mapSpireRole("SITE_ADMINISTRATOR");
    assertThat(role).isPresent().contains(Role.ADMIN);
  }

  @Test
  public void submitterRoleMappingTest() throws Exception {
    Optional<Role> role = SpireUserRolesAdapter.mapSpireRole("APPLICATION_SUBMITTER");
    assertThat(role).isPresent().contains(Role.SUBMITTER);
  }

  @Test
  public void preparerRoleMappingTest() throws Exception {
    Optional<Role> role = SpireUserRolesAdapter.mapSpireRole("APPLICATION_PREPARER");
    assertThat(role).isPresent().contains(Role.PREPARER);
  }

  @Test
  public void unknownRoleMappingTest() throws Exception {
    Optional<Role> role = SpireUserRolesAdapter.mapSpireRole("SOMETHING");
    assertThat(role).isEmpty();
  }

  @Test
  public void emptyRoleMappingTest() throws Exception {
    Optional<Role> role = SpireUserRolesAdapter.mapSpireRole("");
    assertThat(role).isEmpty();
  }
}
