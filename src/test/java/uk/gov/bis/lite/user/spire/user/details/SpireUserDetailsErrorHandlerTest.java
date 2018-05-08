package uk.gov.bis.lite.user.spire.user.details;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.bis.lite.user.spire.SpireResponseTestUtils.createSpireResponse;

import org.junit.Test;
import uk.gov.bis.lite.common.spire.client.SpireResponse;
import uk.gov.bis.lite.common.spire.client.errorhandler.ErrorNodeErrorHandler;
import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;
import uk.gov.bis.lite.user.spire.SpireUserNotFoundException;

public class SpireUserDetailsErrorHandlerTest {

  private final ErrorNodeErrorHandler errorHandler = new SpireUserDetailsErrorHandler();

  @Test
  public void userIdDoesNotExistTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_DETAILS/UserIdDoesNotExist.xml"));

    assertThatThrownBy(() -> errorHandler.checkResponse(response))
        .isExactlyInstanceOf(SpireUserNotFoundException.class)
        .hasMessageContaining("User not found: \"User provided not found.\"");
  }

  @Test
  public void unhandledErrorTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_DETAILS/UnhandledError.xml"));

    assertThatThrownBy(() -> errorHandler.checkResponse(response))
        .isInstanceOf(SpireClientException.class)
        .hasMessageContaining("Unhandled error: \"Some other error.\"");
  }

  @Test
  public void noErrorTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_DETAILS/ValidUserDetails.xml"));

    errorHandler.checkResponse(response);
  }
}