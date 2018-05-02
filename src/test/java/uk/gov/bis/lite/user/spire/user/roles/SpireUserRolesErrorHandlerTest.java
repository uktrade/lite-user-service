package uk.gov.bis.lite.user.spire.user.roles;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.gov.bis.lite.user.spire.SpireResponseTestUtils.createSpireResponse;

import org.junit.Before;
import org.junit.Test;
import uk.gov.bis.lite.common.spire.client.SpireResponse;
import uk.gov.bis.lite.common.spire.client.errorhandler.ErrorNodeErrorHandler;
import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;
import uk.gov.bis.lite.user.spire.SpireUserNotFoundException;

public class SpireUserRolesErrorHandlerTest {

  private ErrorNodeErrorHandler errorHandler;

  @Before
  public void setUp() throws Exception {
    errorHandler = new SpireUserRolesErrorHandler();
  }

  @Test
  public void userIdDoesNotExistTest() throws Exception {
    String errorText = "Web user account for provided userId not found.";
    assertThatThrownBy(() -> errorHandler.handleError(errorText))
        .isInstanceOf(SpireUserNotFoundException.class)
        .hasMessageContaining("User not found: \"Web user account for provided userId not found.\"");
  }

  @Test
  public void userIdDoesNotExistResponseTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/UserIdDoesNotExist.xml"));
    assertThatThrownBy(() -> errorHandler.checkResponse(response))
        .isInstanceOf(SpireUserNotFoundException.class)
        .hasMessageContaining("User not found: \"Web user account for provided userId not found.\"");
  }

  @Test
  public void unhandledErrorTest() throws Exception {
    String errorText = "Some other error.";
    assertThatThrownBy(() -> errorHandler.handleError(errorText))
        .isInstanceOf(SpireClientException.class)
        .hasMessageContaining("Unhandled error: \"Some other error.\"");
  }

  @Test
  public void unhandledErrorResponseTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/UnhandledError.xml"));

    assertThatThrownBy(() -> errorHandler.checkResponse(response))
        .isInstanceOf(SpireClientException.class)
        .hasMessageContaining("Unhandled error: \"Some other error.\"");
  }

  @Test
  public void noErrorTest() throws Exception {
    SpireResponse response = createSpireResponse(fixture("fixture/spire/SPIRE_USER_ROLES/SingleSarAdmin.xml"));

    errorHandler.checkResponse(response);
  }
}