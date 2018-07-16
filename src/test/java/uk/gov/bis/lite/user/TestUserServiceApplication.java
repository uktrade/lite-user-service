package uk.gov.bis.lite.user;

import com.google.inject.Module;
import uk.gov.bis.lite.user.config.GuiceModule;

public class TestUserServiceApplication extends UserServiceApplication {

  public TestUserServiceApplication() {
    super(new Module[]{new GuiceModule(), new TestServiceModule()});
  }

}
