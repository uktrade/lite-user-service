package uk.gov.bis.lite.user.pact;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactBroker;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import ru.vyarus.dropwizard.guice.injector.lookup.InjectorLookup;
import uk.gov.bis.lite.user.config.UserServiceConfiguration;

@RunWith(PactRunner.class)
@Provider("lite-user-service")
@PactBroker(host = "pact-broker.ci.uktrade.io", port = "80")
public class PactProvider {

  @ClassRule
  public static final DropwizardAppRule<UserServiceConfiguration> RULE =
      new DropwizardAppRule<>(PactUserServiceApplication.class, "test-config.yaml",
          ConfigOverride.config("servicePassword", "password"));

  @TestTarget
  public final Target target = new HttpTarget(RULE.getLocalPort());

  @State("provided user exists")
  public void createUserExistsState() {
    InjectorLookup.getInjector(RULE.getApplication()).get().getInstance(MockUserDetailsService.class).setUserExists(true);
    InjectorLookup.getInjector(RULE.getApplication()).get().getInstance(MockUserPrivilegesService.class).setUserExists(true);
  }

  @State("provided user does not exist")
  public void createUserDoesNotExistState() {
    InjectorLookup.getInjector(RULE.getApplication()).get().getInstance(MockUserDetailsService.class).setUserExists(false);
    InjectorLookup.getInjector(RULE.getApplication()).get().getInstance(MockUserPrivilegesService.class).setUserExists(false);
  }

}

