package uk.gov.bis.lite.user.pact;

import com.google.inject.Singleton;
import uk.gov.bis.lite.user.service.UserDetailsService;
import uk.gov.bis.lite.user.spire.user.details.SpireUserDetails;

import java.util.Optional;

@Singleton
public class MockUserDetailsService implements UserDetailsService {

  private boolean userExists;

  @Override
  public Optional<SpireUserDetails> getUserDetails(String userId) {
    if (userExists) {
      SpireUserDetails spireUserDetails = new SpireUserDetails();
      spireUserDetails.setTitle("title");
      spireUserDetails.setFirstName("firstName");
      spireUserDetails.setLastName("lastName");
      spireUserDetails.setFullName("fullName");
      spireUserDetails.setContactEmailAddress("test@test.com");
      spireUserDetails.setContactPhoneNumber("012345678");
      spireUserDetails.setAccountType("");
      spireUserDetails.setAccountStatus("APPLICATION_SUBMITTER");
      return Optional.of(spireUserDetails);
    } else {
      return Optional.empty();
    }
  }

  public void setUserExists(boolean userExists) {
    this.userExists = userExists;
  }

}
