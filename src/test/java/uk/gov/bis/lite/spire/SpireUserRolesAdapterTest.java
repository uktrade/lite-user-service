package uk.gov.bis.lite.spire;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.*;

import org.junit.Test;
import uk.gov.bis.lite.user.api.CustomerView;
import uk.gov.bis.lite.user.api.Role;
import uk.gov.bis.lite.user.api.SiteView;
import uk.gov.bis.lite.user.api.UserPrivilegesView;
import uk.gov.bis.lite.user.spire.SpireUserRole;
import uk.gov.bis.lite.user.spire.SpireUserRolesAdapter;

import java.util.Arrays;
import java.util.List;

public class SpireUserRolesAdapterTest {

  @Test
  public void customerIsAdminTest() throws Exception {
    List<SpireUserRole> roleList = singletonList(buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerIsSubmitterTest() throws Exception {
    List<SpireUserRole> roleList = singletonList(buildCustomerSubmitter("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerIsPreparerTest() throws Exception {
    List<SpireUserRole> roleList = singletonList(buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void siteIsAdminTest() throws Exception {
    List<SpireUserRole> roleList = singletonList(buildSiteAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SAR123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteIsSubmitterTest() throws Exception {
    List<SpireUserRole> roleList = singletonList(buildSiteSubmitter("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteIsPreparerTest() throws Exception {
    List<SpireUserRole> roleList = singletonList(buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void customerAndSiteTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(buildCustomerAdmin("SAR123"), buildSiteAdmin("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
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
    List<SpireUserRole> roleList = Arrays.asList(
        buildCustomerAdmin("SAR123"),
        buildCustomerAdmin("SAR456"),
        buildSiteAdmin("SITE123"),
        buildSiteAdmin("SITE456"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
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
    List<SpireUserRole> roleList = Arrays.asList(
        buildSiteAdmin("SITE456"),
        buildSiteAdmin("SITE123"),
        buildCustomerAdmin("SAR456"),
        buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
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
    List<SpireUserRole> roleList = Arrays.asList(
        buildCustomerAdmin("SAR123"),
        buildCustomerSubmitter("SAR123"),
        buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerRoleAdminPriorityReversedTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildCustomerPreparer("SAR123"),
        buildCustomerSubmitter("SAR123"),
        buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteRoleAdminPriorityTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildSiteAdmin("SITE123"),
        buildSiteSubmitter("SITE123"),
        buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteRoleAdminPriorityReversedTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildSitePreparer("SITE123"),
        buildSiteSubmitter("SITE123"),
        buildSiteAdmin("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerRoleSubmitterPriorityTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildCustomerSubmitter("SAR123"),
        buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerRoleSubmitterPriorityReversedTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildCustomerPreparer("SAR123"),
        buildCustomerSubmitter("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteRoleSubmitterPriorityTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildSiteSubmitter("SITE123"),
        buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteRoleAdminSubmitterReversedTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildSitePreparer("SITE123"),
        buildSiteSubmitter("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerIgnoreDuplicateAdminTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildCustomerAdmin("SAR123"),
        buildCustomerAdmin("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void siteIgnoreDuplicateAdminTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildSiteAdmin("SITE123"),
        buildSiteAdmin("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  public void customerIgnoreDuplicateSubmitterTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildCustomerSubmitter("SAR123"),
        buildCustomerSubmitter("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void siteIgnoreDuplicateSubmitterTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildSiteSubmitter("SITE123"),
        buildSiteSubmitter("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.SUBMITTER);
  }

  @Test
  public void customerIgnoreDuplicatePreparerTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildCustomerPreparer("SAR123"),
        buildCustomerPreparer("SAR123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().isEmpty()).isTrue();

    CustomerView customer = userPrivs.getCustomers().get(0);
    assertThat(customer.getCustomerId()).isEqualTo("SAR123");
    assertThat(customer.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void siteIgnoreDuplicatePreparerTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildSitePreparer("SITE123"),
        buildSitePreparer("SITE123"));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();

    SiteView site = userPrivs.getSites().get(0);
    assertThat(site.getSiteId()).isEqualTo("SITE123");
    assertThat(site.getRole()).isEqualTo(Role.PREPARER);
  }

  @Test
  public void ignoreUnusedOrEmptyRolesTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName("SOME_OTHER_ROLE")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName("")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName(null)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName("SOME_OTHER_ROLE")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName("")
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName(null)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build()
        );

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void ignoreEmptySarRefs() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildCustomerAdmin(""),
        buildCustomerAdmin(null));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void ignoreEmptySiteRefs() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        buildSiteAdmin(""),
        buildSiteAdmin(null));

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void ignoreEmptyResType() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        SpireUserRole.builder()
            .setResType("")
            .setSarRef("SAR123")
            .setRoleName(ROLE_SAR_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType("")
            .setSiteRef("SITE123")
            .setRoleName(ROLE_SITE_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(null)
            .setSarRef("SAR123")
            .setRoleName(ROLE_SAR_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build(),
        SpireUserRole.builder()
            .setResType(null)
            .setSiteRef("SITE123")
            .setRoleName(ROLE_SITE_ADMINISTRATOR)
            .setIsAdmin("Y")
            .setIsApplicant("N")
            .setFullName(FULL_NAME)
            .build()
    );

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().isEmpty()).isTrue();
    assertThat(userPrivs.getSites().isEmpty()).isTrue();
  }

  @Test
  public void tolerantOfUnusedFieldTest() throws Exception {
    List<SpireUserRole> roleList = Arrays.asList(
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SAR_USERS)
            .setSarRef("SAR123")
            .setRoleName(ROLE_SAR_ADMINISTRATOR)
            .build(),
        SpireUserRole.builder()
            .setResType(RES_TYPE_SPIRE_SITE_USERS)
            .setSiteRef("SITE123")
            .setRoleName(ROLE_SITE_ADMINISTRATOR)
            .build()
        );

    UserPrivilegesView userPrivs = SpireUserRolesAdapter.adapt(roleList);
    assertThat(userPrivs.getCustomers().size()).isEqualTo(1);
    assertThat(userPrivs.getSites().size()).isEqualTo(1);
  }
}
