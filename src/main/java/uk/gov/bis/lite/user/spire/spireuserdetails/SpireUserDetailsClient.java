package uk.gov.bis.lite.user.spire.spireuserdetails;

import uk.gov.bis.lite.common.spire.client.SpireClient;
import uk.gov.bis.lite.common.spire.client.SpireClientConfig;
import uk.gov.bis.lite.common.spire.client.SpireRequestConfig;
import uk.gov.bis.lite.common.spire.client.errorhandler.ErrorHandler;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;

public class SpireUserDetailsClient extends SpireClient<SpireUserDetails> {
  public SpireUserDetailsClient(SpireParser<SpireUserDetails> parser, SpireClientConfig clientConfig, SpireRequestConfig requestConfig, ErrorHandler errorHandler) {
    super(parser, clientConfig, requestConfig, errorHandler);
  }
}
