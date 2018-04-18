package uk.gov.bis.lite.user.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import uk.gov.bis.lite.user.UserServiceApplication;
import uk.gov.bis.lite.user.config.UserServiceConfiguration;

public class BaseIntegrationTest {

  @ClassRule
  public static final WireMockClassRule wireMockClassRule = new WireMockClassRule(options().dynamicPort());

  @Rule
  public DropwizardAppRule<UserServiceConfiguration> RULE = new DropwizardAppRule<>(
      UserServiceApplication.class, "test-config.yaml",
      ConfigOverride.config("spireClientUrl", "http://localhost:" + wireMockClassRule.port() + "/spire/fox/ispire/"));

  @BeforeClass
  public static void setUp() throws Exception {
    configureFor(wireMockClassRule.port());
  }
}
