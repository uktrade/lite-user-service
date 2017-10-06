package uk.gov.bis.lite.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import uk.gov.bis.lite.user.UserServiceApplication;
import uk.gov.bis.lite.user.config.UserServiceConfiguration;

import java.util.Collections;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

public class BaseIntegrationTest {

  public static final MultivaluedMap<String, Object> authedHeaders;

  /**
   * Decodes to:
   *
   * <blockquote><pre>
   * {
   *  "typ": "JWT",
   *  "alg": "HS256"
   * }.
   * {
   *  "iss": "Online JWT Builder",
   *  "iat": 1507121580,
   *  "exp": 1538657587,
   *  "aud": "lite",
   *  "sub": "123456",
   *  "email": "example@example.com"
   * }
   * </pre></blockquote>
   *
   * using HMAC SHA-256 with key "demo-secret-which-is-very-long-so-as-to-hit-the-byte-requirement" for signing
   */
  public static final String JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE1MDcxMjE1ODAsImV4cCI6MTUzODY1NzU4NywiYXVkIjoibGl0ZSIsInN1YiI6IjEyMzQ1NiIsImVtYWlsIjoiZXhhbXBsZUBleGFtcGxlLmNvbSJ9.wUKTzHkQoym-KCWzFUFrXeEKRQ3y3to-CBeHbqOxW4s";

  static {
    authedHeaders = new MultivaluedHashMap<>();
    authedHeaders.put("Authorization", Collections.singletonList("Bearer " + JWT));
  }

  @ClassRule
  public static final WireMockClassRule wireMockClassRule = new WireMockClassRule(options().dynamicPort());

  @Rule
  public DropwizardAppRule<UserServiceConfiguration> RULE = new DropwizardAppRule<>(
      UserServiceApplication.class, resourceFilePath("test-config.yaml"),
      ConfigOverride.config("spireClientUrl", "http://localhost:" +  wireMockClassRule.port() + "/spire/fox/ispire/"));

  @BeforeClass
  public static void setUp() throws Exception {
    configureFor(wireMockClassRule.port());
  }
}
