package uk.gov.bis.lite.user.spire.spireuserroles;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.bis.lite.user.spire.SpireResponseTestUtils.createSpireResponse;

import org.junit.Before;
import org.junit.Test;
import uk.gov.bis.lite.common.spire.client.SpireResponse;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;

public class SpireUserRolesParserTest {

  private SpireParser<SpireUserRoles> parser;

  @Before
  public void setUp() throws Exception {
    parser = new SpireUserRolesParser();
  }

  @Test
  public void singleSarAdminTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSarAdmin.xml"));

    SpireUserRoles spireUserRoles = parser.parseResponse(response);
    assertThat(spireUserRoles.getUserRoles().size()).isEqualTo(1);

    SpireUserRole sur = spireUserRoles.getUserRoles().get(0);
    assertThat(sur.getResType()).isEqualTo("SPIRE_SAR_USERS");
    assertThat(sur.getRoleName()).isEqualTo("SAR_ADMINISTRATOR");
    assertThat(sur.getFullName()).isEqualTo("Mr Test");
    assertThat(sur.getSarRef()).isEqualTo("SAR123");
    assertThat(sur.getIsAdmin()).isEqualTo("Y");
    assertThat(sur.getIsApplicant()).isEqualTo("N");
  }

  @Test
  public void singleSiteAdminTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSiteAdmin.xml"));

    SpireUserRoles spireUserRoles = parser.parseResponse(response);
    assertThat(spireUserRoles.getUserRoles().size()).isEqualTo(1);

    SpireUserRole sur = spireUserRoles.getUserRoles().get(0);
    assertThat(sur.getResType()).isEqualTo("SPIRE_SITE_USERS");
    assertThat(sur.getRoleName()).isEqualTo("SITE_ADMINISTRATOR");
    assertThat(sur.getFullName()).isEqualTo("Mr Test");
    assertThat(sur.getSiteRef()).isEqualTo("SITE123");
    assertThat(sur.getIsAdmin()).isEqualTo("Y");
    assertThat(sur.getIsApplicant()).isEqualTo("N");
  }

  @Test
  public void SarAndSiteAdminTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/SarAndSiteAdmin.xml"));

    SpireUserRoles spireUserRoles = parser.parseResponse(response);
    assertThat(spireUserRoles.getUserRoles().size()).isEqualTo(2);

    SpireUserRole sur = spireUserRoles.getUserRoles().get(0);
    assertThat(sur.getResType()).isEqualTo("SPIRE_SAR_USERS");
    assertThat(sur.getRoleName()).isEqualTo("SAR_ADMINISTRATOR");
    assertThat(sur.getFullName()).isEqualTo("Mr Test");
    assertThat(sur.getSarRef()).isEqualTo("SAR123");
    assertThat(sur.getIsAdmin()).isEqualTo("Y");
    assertThat(sur.getIsApplicant()).isEqualTo("N");

    sur = spireUserRoles.getUserRoles().get(1);
    assertThat(sur.getResType()).isEqualTo("SPIRE_SITE_USERS");
    assertThat(sur.getRoleName()).isEqualTo("SITE_ADMINISTRATOR");
    assertThat(sur.getFullName()).isEqualTo("Mr Test");
    assertThat(sur.getSiteRef()).isEqualTo("SITE123");
    assertThat(sur.getIsAdmin()).isEqualTo("Y");
    assertThat(sur.getIsApplicant()).isEqualTo("N");
  }

  @Test
  public void junkRoleTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/JunkRole.xml"));

    SpireUserRoles spireUserRoles = parser.parseResponse(response);
    assertThat(spireUserRoles.getUserRoles().size()).isEqualTo(1);

    SpireUserRole sur = spireUserRoles.getUserRoles().get(0);
    assertThat(sur.getResType()).isEqualTo("lkvdLQUFmpnYWDBueprb");
    assertThat(sur.getRoleName()).isEqualTo("CPRnydOuaOHRHiIHJqMg");
    assertThat(sur.getFullName()).isEqualTo("zSmRtJFHnfkIgKDtNwvO");
    assertThat(sur.getSarRef()).isEqualTo("LEROZQOLsJkUkaKBaFlz");
    assertThat(sur.getSiteRef()).isEqualTo("ZbZCAxWTbdfIDefwpUmX");
    assertThat(sur.getIsAdmin()).isEqualTo("crlvblOjtwtGhSoMcRgB");
    assertThat(sur.getIsApplicant()).isEqualTo("nhqgcWLWCIVtssZwvdrb");
  }

  @Test
  public void noRolesTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/NoRoles.xml"));

    SpireUserRoles spireUserRoles = parser.parseResponse(response);

    assertThat(spireUserRoles.getUserRoles().isEmpty()).isTrue();
  }

  @Test
  public void userIdDoesNotExistTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/UserIdDoesNotExist.xml"));

    SpireUserRoles spireUserRoles = parser.parseResponse(response);

    assertThat(spireUserRoles.getUserRoles().isEmpty()).isTrue();
  }

  @Test
  public void unhandledErrorTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/UnhandledError.xml"));

    SpireUserRoles spireUserRoles = parser.parseResponse(response);

    assertThat(spireUserRoles.getUserRoles().isEmpty()).isTrue();
  }
}
