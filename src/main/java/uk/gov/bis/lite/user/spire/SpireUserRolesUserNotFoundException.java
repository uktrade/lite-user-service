package uk.gov.bis.lite.user.spire;

import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;

public class SpireUserRolesUserNotFoundException extends SpireClientException {
  public SpireUserRolesUserNotFoundException(String info) {
    super(info);
  }

  public SpireUserRolesUserNotFoundException(String info, Throwable cause) {
    super(info, cause);
  }
}
