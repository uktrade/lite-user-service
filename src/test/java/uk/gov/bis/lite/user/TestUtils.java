package uk.gov.bis.lite.user;

import uk.gov.bis.lite.common.jwt.LiteJwtConfig;
import uk.gov.bis.lite.common.jwt.LiteJwtUser;
import uk.gov.bis.lite.common.jwt.LiteJwtUserHelper;

import java.util.Base64;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

public class TestUtils {
  public static final String JWT_SHARED_SECRET = "demo-secret-which-is-very-long-so-as-to-hit-the-byte-requirement";

  private static final LiteJwtUserHelper liteJwtUserHelper = new LiteJwtUserHelper(new LiteJwtConfig(JWT_SHARED_SECRET, "some-lite-service"));

  private TestUtils(){}

  public static String generateJwtAuthorizationHeader(String userId, String email, String fullName) {
    LiteJwtUser liteJwtUser = new LiteJwtUser()
        .setEmail(email)
        .setFullName(fullName)
        .setUserId(userId);
    return "Bearer " + liteJwtUserHelper.generateToken(liteJwtUser);
  }

  public static String generateBasicAuthorizationHeader(String username, String password) {
    return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
  }

  public static Map<String, String> getMapFromResponse(Response response) {
    return response.readEntity(new GenericType<Map<String, String>>() {
    });
  }
}
