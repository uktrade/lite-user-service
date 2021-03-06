package uk.gov.bis.lite.user.spire.user.roles;

import uk.gov.bis.lite.common.spire.client.errorhandler.ErrorNodeErrorHandler;
import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;
import uk.gov.bis.lite.user.spire.SpireUserNotFoundException;

public class SpireUserRolesErrorHandler extends ErrorNodeErrorHandler {

  public SpireUserRolesErrorHandler() {
    super("//*[local-name()='ROLE_LIST']");
}

  @Override
  public void handleError(String errorText) {
    if (errorText.matches("Web user account for provided (userId|loginId) not found.")) {
      throw new SpireUserNotFoundException("User not found: \"" + errorText + "\"");
    } else {
      throw new SpireClientException("Unhandled error: \"" + errorText + "\"");
    }
  }
}
