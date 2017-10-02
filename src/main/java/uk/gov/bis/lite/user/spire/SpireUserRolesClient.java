package uk.gov.bis.lite.user.spire;

import uk.gov.bis.lite.common.spire.client.SpireClient;
import uk.gov.bis.lite.common.spire.client.SpireClientConfig;
import uk.gov.bis.lite.common.spire.client.SpireRequest;
import uk.gov.bis.lite.common.spire.client.SpireRequestConfig;
import uk.gov.bis.lite.common.spire.client.errorhandler.ErrorHandler;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;

import java.util.List;
import java.util.Optional;

public class SpireUserRolesClient extends SpireClient<List<SpireUserRole>> {
  public SpireUserRolesClient(SpireParser<List<SpireUserRole>> parser, SpireClientConfig clientConfig, SpireRequestConfig requestConfig, ErrorHandler errorHandler) {
    super(parser, clientConfig, requestConfig, errorHandler);
  }

  private SpireRequest createRequest(String userId) {
    SpireRequest request = createRequest();
    request.addChild("userId", userId);
    return request;
  }

  public Optional<List<SpireUserRole>> sendRequest(String userId) {
    try {
      return Optional.of(sendRequest(createRequest(userId)));
    }
    catch (SpireUserRolesUserNotFoundException e) {
      return Optional.empty();
    }
  }
}
