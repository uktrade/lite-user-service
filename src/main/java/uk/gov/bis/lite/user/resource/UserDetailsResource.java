package uk.gov.bis.lite.user.resource;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import org.apache.commons.lang3.StringUtils;
import uk.gov.bis.lite.common.jwt.LiteJwtUser;
import uk.gov.bis.lite.user.api.view.UserDetailsView;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetailsAdapter;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user-details")
public class UserDetailsResource {
  private final UserDetailsService userDetailsService;

  @Inject
  public UserDetailsResource(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @GET
  @Produces({MediaType.APPLICATION_JSON})
  @Path("/{userId}")
  public UserDetailsView viewUserDetails(@PathParam("userId") String userId, @Auth LiteJwtUser user) {
    if (!StringUtils.equals(user.getUserId(), userId)) {
      throw new WebApplicationException(String.format("userId %s does not match value supplied in token %s", userId,
          user.getUserId()), Response.Status.UNAUTHORIZED);
    }
    Optional<UserDetailsView> userDetails = userDetailsService.getUserDetails(userId)
        .map(SpireUserDetailsAdapter::mapToUserDetailsView);
    if (userDetails.isPresent()) {
      return userDetails.get();
    } else {
      throw new WebApplicationException("User not found", Response.Status.NOT_FOUND);
    }
  }
}
