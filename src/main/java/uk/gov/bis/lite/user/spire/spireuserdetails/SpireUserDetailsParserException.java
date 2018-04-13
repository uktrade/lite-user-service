package uk.gov.bis.lite.user.spire.spireuserdetails;

import javax.ws.rs.WebApplicationException;

public class SpireUserDetailsParserException extends WebApplicationException {
  public SpireUserDetailsParserException(String message) {
    super(message);
  }
}
