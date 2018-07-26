package uk.gov.bis.lite.user.pact;

import com.google.inject.Module;
import uk.gov.bis.lite.user.UserServiceApplication;
import uk.gov.bis.lite.user.config.GuiceModule;

public class PactUserServiceApplication extends UserServiceApplication {

  public PactUserServiceApplication() {
    super(new Module[]{new GuiceModule(), new PactServiceModule()});
  }

}
