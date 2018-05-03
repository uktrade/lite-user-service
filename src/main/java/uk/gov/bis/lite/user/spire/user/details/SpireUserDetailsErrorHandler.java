package uk.gov.bis.lite.user.spire.user.details;

import uk.gov.bis.lite.common.spire.client.errorhandler.ErrorNodeErrorHandler;
import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;
import uk.gov.bis.lite.user.spire.SpireUserNotFoundException;

public class SpireUserDetailsErrorHandler extends ErrorNodeErrorHandler {

  public SpireUserDetailsErrorHandler() {
    super("//*[local-name()='USER_DETAILS_LIST']");
  }

  @Override
  public void handleError(String errorText) {
    if (errorText.contains("User provided not found.")) {
      throw new SpireUserNotFoundException("User not found: \"" + errorText + "\"");
    } else {
      throw new SpireClientException("Unhandled error: \"" + errorText + "\"");
    }
  }
}
