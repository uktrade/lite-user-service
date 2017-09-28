package uk.gov.bis.lite.spire;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.bis.lite.spire.SpireUserRolesUtil.stubForBody;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import uk.gov.bis.lite.common.spire.client.SpireClientConfig;
import uk.gov.bis.lite.common.spire.client.SpireRequestConfig;
import uk.gov.bis.lite.common.spire.client.errorhandler.ErrorHandler;
import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;
import uk.gov.bis.lite.user.spire.SpireUserRole;
import uk.gov.bis.lite.user.spire.SpireUserRolesClient;
import uk.gov.bis.lite.user.spire.SpireUserRolesErrorHandler;
import uk.gov.bis.lite.user.spire.SpireUserRolesParser;

import java.util.List;
import java.util.Optional;

public class SpireUserRolesClientTest {

  private SpireParser<List<SpireUserRole>> parser = new SpireUserRolesParser();

  private SpireClientConfig config = new SpireClientConfig("username", "password", "http://localhost:8080/spire/fox/ispire/");

  private SpireRequestConfig requestConfig = new SpireRequestConfig("SPIRE_USER_ROLES", "getRoles", true);

  private ErrorHandler errorHandler = new SpireUserRolesErrorHandler();

  private SpireUserRolesClient client = new SpireUserRolesClient(parser, config, requestConfig, errorHandler);

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(8080);

  @Test
  public void singleSarAdminTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSarAdmin.xml"));

    Optional<List<SpireUserRole>> spireUserRolesOpt = client.sendRequest("123");

    assertThat(spireUserRolesOpt.isPresent()).isTrue();
    List<SpireUserRole> spireUserRoles = spireUserRolesOpt.get();
    assertThat(spireUserRoles.size()).isEqualTo(1);

    SpireUserRole sur = spireUserRoles.get(0);
    assertThat(sur.getResType()).isEqualTo("SPIRE_SAR_USERS");
    assertThat(sur.getRoleName()).isEqualTo("SAR_ADMINISTRATOR");
    assertThat(sur.getFullName()).isEqualTo("Mr Test");
    assertThat(sur.getSarRef()).isEqualTo("SAR123");
    assertThat(sur.getIsAdmin()).isEqualTo("Y");
    assertThat(sur.getIsApplicant()).isEqualTo("N");
  }

  @Test
  public void singleSiteAdminTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSiteAdmin.xml"));

    Optional<List<SpireUserRole>> spireUserRolesOpt = client.sendRequest("123");

    assertThat(spireUserRolesOpt.isPresent()).isTrue();
    List<SpireUserRole> spireUserRoles = spireUserRolesOpt.get();
    assertThat(spireUserRoles.size()).isEqualTo(1);

    SpireUserRole sur = spireUserRoles.get(0);
    assertThat(sur.getResType()).isEqualTo("SPIRE_SITE_USERS");
    assertThat(sur.getRoleName()).isEqualTo("SITE_ADMINISTRATOR");
    assertThat(sur.getFullName()).isEqualTo("Mr Test");
    assertThat(sur.getSiteRef()).isEqualTo("SITE123");
    assertThat(sur.getIsAdmin()).isEqualTo("Y");
    assertThat(sur.getIsApplicant()).isEqualTo("N");
  }

  @Test
  public void SarAndSiteAdminTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/SarAndSiteAdmin.xml"));

    Optional<List<SpireUserRole>> spireUserRolesOpt = client.sendRequest("123");

    assertThat(spireUserRolesOpt.isPresent()).isTrue();
    List<SpireUserRole> spireUserRoles = spireUserRolesOpt.get();
    assertThat(spireUserRoles.size()).isEqualTo(2);

    SpireUserRole sur = spireUserRoles.get(0);
    assertThat(sur.getResType()).isEqualTo("SPIRE_SAR_USERS");
    assertThat(sur.getRoleName()).isEqualTo("SAR_ADMINISTRATOR");
    assertThat(sur.getFullName()).isEqualTo("Mr Test");
    assertThat(sur.getSarRef()).isEqualTo("SAR123");
    assertThat(sur.getIsAdmin()).isEqualTo("Y");
    assertThat(sur.getIsApplicant()).isEqualTo("N");

    sur = spireUserRoles.get(1);
    assertThat(sur.getResType()).isEqualTo("SPIRE_SITE_USERS");
    assertThat(sur.getRoleName()).isEqualTo("SITE_ADMINISTRATOR");
    assertThat(sur.getFullName()).isEqualTo("Mr Test");
    assertThat(sur.getSiteRef()).isEqualTo("SITE123");
    assertThat(sur.getIsAdmin()).isEqualTo("Y");
    assertThat(sur.getIsApplicant()).isEqualTo("N");
  }

  @Test
  public void junkRoleTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/JunkRole.xml"));

    Optional<List<SpireUserRole>> spireUserRolesOpt = client.sendRequest("123");

    assertThat(spireUserRolesOpt.isPresent()).isTrue();
    List<SpireUserRole> spireUserRoles = spireUserRolesOpt.get();
    assertThat(spireUserRoles.size()).isEqualTo(1);

    SpireUserRole sur = spireUserRoles.get(0);
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
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/NoRoles.xml"));

    Optional<List<SpireUserRole>> spireUserRolesOpt = client.sendRequest("123");

    assertThat(spireUserRolesOpt.isPresent()).isTrue();
    List<SpireUserRole> spireUserRoles = spireUserRolesOpt.get();
    assertThat(spireUserRoles.isEmpty()).isTrue();
  }

  @Test
  public void userIdDoesNotExistTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/UserIdDoesNotExist.xml"));

    Optional<List<SpireUserRole>> spireUserRolesOpt = client.sendRequest("123");

    assertThat(spireUserRolesOpt.isPresent()).isFalse();
  }

  @Test
  public void unhandledErrorTest() throws Exception {
    stubForBody(fixture("fixture/spire/SPIRE_USER_ROLES/UnhandledError.xml"));

    assertThatThrownBy(() -> client.sendRequest("123"))
        .isInstanceOf(SpireClientException.class)
        .hasMessageContaining("Unhandled error: \"Some other error.\"");
  }

  @Test
  public void spireError500() throws Exception {
    stubFor(post(urlEqualTo("/spire/fox/ispire/SPIRE_USER_ROLES"))
        .willReturn(aResponse()
            .withStatus(500)));

    assertThatThrownBy(() -> client.sendRequest("123"))
        .isInstanceOf(SpireClientException.class)
        .hasMessageContaining("SOAP");
  }
}
