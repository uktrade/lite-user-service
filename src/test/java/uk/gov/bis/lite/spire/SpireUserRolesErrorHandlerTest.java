package uk.gov.bis.lite.spire;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;
import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;
import uk.gov.bis.lite.user.spire.SpireUserRolesErrorHandler;
import uk.gov.bis.lite.user.spire.SpireUserRolesUserNotFoundException;

public class SpireUserRolesErrorHandlerTest {

  private final SpireUserRolesErrorHandler errorHandler = new SpireUserRolesErrorHandler();

  @Test
  public void userIdDoesNotExistTest() throws Exception {
    String errorText = "Web user account for provided userId not found.";
    assertThatThrownBy(() -> errorHandler.handleError(errorText))
        .isInstanceOf(SpireUserRolesUserNotFoundException.class)
        .hasMessageContaining("User not found: \"Web user account for provided userId not found.\"");
  }

  @Test
  public void unhandledErrorTest() throws Exception {
    String errorText = "Some other error.";
    assertThatThrownBy(() -> errorHandler.handleError(errorText))
        .isInstanceOf(SpireClientException.class)
        .hasMessageContaining("Unhandled error: \"Some other error.\"");
  }
}