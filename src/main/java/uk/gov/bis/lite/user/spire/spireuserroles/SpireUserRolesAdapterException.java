package uk.gov.bis.lite.user.spire.spireuserroles;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class SpireUserRolesAdapterException extends WebApplicationException {

  /**
   * SpireUserRolesAdapterException
   *
   * @param info information on exception
   */
  public SpireUserRolesAdapterException(String info) {
    super("SpireUserRoles Adapter Exception: " + info, Response.Status.BAD_REQUEST);
  }

  /**
   * SpireUserRolesAdapterException
   *
   * @param info  information on exception
   * @param cause the cause
   */
  public SpireUserRolesAdapterException(String info, Throwable cause) {
    super("SpireUserRoles Adapter Exception: " + info, cause, Response.Status.BAD_REQUEST);
  }
}
