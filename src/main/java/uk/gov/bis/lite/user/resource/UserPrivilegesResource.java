package uk.gov.bis.lite.user.resource;

import com.google.inject.Inject;
import uk.gov.bis.lite.user.api.UserPrivilegesView;
import uk.gov.bis.lite.user.service.UserPrivilegesService;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user-privileges")
public class UserPrivilegesResource {

  private final UserPrivilegesService userPrivilegesService;

  @Inject
  public UserPrivilegesResource(UserPrivilegesService userPrivilegesService) {
    this.userPrivilegesService = userPrivilegesService;
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @Path("/user/{userId}")
  public Optional<UserPrivilegesView> viewUserPrivileges(@PathParam("userId") String userId) {
    return userPrivilegesService.getUserPrivileges(userId);
  }
}
