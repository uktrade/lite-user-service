package uk.gov.bis.lite.user.spire.spireuserdetails;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.bis.lite.user.spire.SpireResponseTestUtils.createSpireResponse;

import org.junit.Test;
import uk.gov.bis.lite.common.spire.client.SpireResponse;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;

public class SpireUserDetailsParserTest {

  private SpireParser<SpireUserDetails> parser = new SpireUserDetailsParser();

  @Test
  public void emptyUserDetailsTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_DETAILS/EmptyUserDetails.xml"));

    SpireUserDetails spireUserDetails = parser.parseResponse(response);

    assertThat(spireUserDetails.getTitle()).isNull();
    assertThat(spireUserDetails.getFirstName()).isNull();
    assertThat(spireUserDetails.getLastName()).isNull();
    assertThat(spireUserDetails.getFullName()).isNull();
    assertThat(spireUserDetails.getContactEmailAddress()).isNull();
    assertThat(spireUserDetails.getContactPhoneNumber()).isNull();
    assertThat(spireUserDetails.getAccountType()).isNull();
    assertThat(spireUserDetails.getAccountStatus()).isNull();
  }

  @Test
  public void emptyUserDetailsListTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_DETAILS/EmptyUserDetailsList.xml"));

    assertThatThrownBy(() -> parser.parseResponse(response))
        .isExactlyInstanceOf(SpireUserDetailsParserException.class)
        .hasMessageContaining("Unexpected number of USER_DETAILS found while parsing the SOAP response body, expected 1 but got: 0");
  }

  @Test
  public void multipleUserDetailsTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_DETAILS/MultipleUserDetails.xml"));

    assertThatThrownBy(() -> parser.parseResponse(response))
        .isExactlyInstanceOf(SpireUserDetailsParserException.class)
        .hasMessageContaining("Unexpected number of USER_DETAILS found while parsing the SOAP response body, expected 1 but got: 2");
  }

  @Test
  public void unhandledErrorTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_DETAILS/UnhandledError.xml"));

    assertThatThrownBy(() -> parser.parseResponse(response))
        .isExactlyInstanceOf(SpireUserDetailsParserException.class)
        .hasMessageContaining("Unexpected element found while parsing the SOAP response body: ERROR");
  }

  @Test
  public void userIdDoesNotExistTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_DETAILS/UserIdDoesNotExist.xml"));

    assertThatThrownBy(() -> parser.parseResponse(response))
        .isExactlyInstanceOf(SpireUserDetailsParserException.class)
        .hasMessageContaining("Unexpected element found while parsing the SOAP response body: ERROR");
  }

  @Test
  public void validUserDetailsTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_DETAILS/ValidUserDetails.xml"));

    SpireUserDetails spireUserDetails = parser.parseResponse(response);

    assertThat(spireUserDetails.getTitle()).isEqualTo("mr");
    assertThat(spireUserDetails.getFirstName()).isEqualTo("applicant1");
    assertThat(spireUserDetails.getLastName()).isEqualTo("test");
    assertThat(spireUserDetails.getFullName()).isEqualTo("mr applicant1 test");
    assertThat(spireUserDetails.getContactEmailAddress()).isEqualTo("applicant1@test.com");
    assertThat(spireUserDetails.getContactPhoneNumber()).isEqualTo("+44 (0)208 123 4567");
    assertThat(spireUserDetails.getAccountType()).isEqualTo("EXPORTER");
    assertThat(spireUserDetails.getAccountStatus()).isEqualTo("ACTIVE");
  }
}