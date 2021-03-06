package uk.gov.bis.lite.user.service;

import uk.gov.bis.lite.user.spire.user.details.SpireUserDetails;

import java.util.Optional;

public interface UserDetailsService {

  Optional<SpireUserDetails> getUserDetails(String userId);

}
