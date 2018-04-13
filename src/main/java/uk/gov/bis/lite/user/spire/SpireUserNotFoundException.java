package uk.gov.bis.lite.user.spire;

import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;

public class SpireUserNotFoundException extends SpireClientException {
  public SpireUserNotFoundException(String info) {
    super(info);
  }

  public SpireUserNotFoundException(String info, Throwable cause) {
    super(info, cause);
  }
}
