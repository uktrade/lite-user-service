package uk.gov.bis.lite.user.spire.user.roles;

import uk.gov.bis.lite.common.spire.client.SpireClient;
import uk.gov.bis.lite.common.spire.client.SpireClientConfig;
import uk.gov.bis.lite.common.spire.client.SpireRequestConfig;
import uk.gov.bis.lite.common.spire.client.errorhandler.ErrorHandler;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;

public class SpireUserRolesClient extends SpireClient<SpireUserRoles> {
  public SpireUserRolesClient(SpireParser<SpireUserRoles> parser, SpireClientConfig clientConfig, SpireRequestConfig requestConfig, ErrorHandler errorHandler) {
    super(parser, clientConfig, requestConfig, errorHandler);
  }
}
