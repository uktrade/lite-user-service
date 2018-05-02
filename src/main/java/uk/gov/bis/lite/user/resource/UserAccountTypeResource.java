package uk.gov.bis.lite.user.resource;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import uk.gov.bis.lite.common.auth.basic.Roles;
import uk.gov.bis.lite.common.auth.basic.User;
import uk.gov.bis.lite.user.api.view.UserAccountTypeView;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsAdapter;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user-account-type")
public class UserAccountTypeResource {
  private final UserDetailsService userDetailsService;

  @Inject
  public UserAccountTypeResource(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @RolesAllowed(Roles.SERVICE)
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/{userId}")
  public UserAccountTypeView viewAccountType(@PathParam("userId") @NotEmpty @Length(max = 10) String userId, @Auth User user) {
    Optional<UserAccountTypeView> accountType = userDetailsService.getUserDetails(userId)
        .map(SpireUserDetailsAdapter::mapToUserAccountTypeView);
    if (accountType.isPresent()) {
      return accountType.get();
    } else {
      throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
    }
  }
}
