package uk.gov.bis.lite.user.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import com.github.tomakehurst.wiremock.junit.WireMockClassRule;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwx.HeaderParameterNames;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
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

  static {
    JwtClaims claims = new JwtClaims();
    claims.setIssuer("Some lite application");
    claims.setExpirationTimeMinutesInTheFuture(10);
    claims.setGeneratedJwtId();
    claims.setIssuedAtToNow();
    claims.setNotBeforeMinutesInThePast(2);
    claims.setSubject("123456");
    claims.setClaim("email", "example@example.com");
    claims.setClaim("fullName", "Mr Test");

    JsonWebSignature jws = new JsonWebSignature();
    jws.setPayload(claims.toJson());
    jws.setKey(new HmacKey("demo-secret-which-is-very-long-so-as-to-hit-the-byte-requirement".getBytes()));
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
    jws.setHeader(HeaderParameterNames.TYPE, "JWT");

    String jwt;
    try {
      jwt = jws.getCompactSerialization();
    } catch (JoseException e) {
      throw new RuntimeException(e);
    }

    authedHeaders = new MultivaluedHashMap<>();
    authedHeaders.put("Authorization", Collections.singletonList("Bearer " + jwt));
  }

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
