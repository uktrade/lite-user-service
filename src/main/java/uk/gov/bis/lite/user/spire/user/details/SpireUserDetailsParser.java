package uk.gov.bis.lite.user.spire.user.details;

import static uk.gov.bis.lite.user.spire.SpireResponseUtils.getNodeValue;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import uk.gov.bis.lite.common.spire.client.SpireResponse;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public class SpireUserDetailsParser implements SpireParser<SpireUserDetails> {
  private static final Logger LOGGER = LoggerFactory.getLogger(SpireUserDetailsParser.class);

  @Override
  public SpireUserDetails parseResponse(SpireResponse spireResponse) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    SpireUserDetails spireUserDetails = doResponseParsing(spireResponse);
    stopwatch.stop();
    LOGGER.info("The unmarshalling of SpireResponse produced SpireUserDetails and took {}ms,", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    return spireUserDetails;
  }

  private SpireUserDetails doResponseParsing(SpireResponse spireResponse) {
    XPath xpath = XPathFactory.newInstance().newXPath();
    List<SpireUserDetails> userDetailsList = spireResponse.getElementChildNodesForList("//*[local-name()='USER_DETAILS_LIST']")
        .stream()
        .map(node -> {
          // Better XPath performance with large DOMs
          Node clonedNode = node.cloneNode(true);
          if ("USER_DETAILS".equals(clonedNode.getNodeName())) {
            SpireUserDetails userDetails = new SpireUserDetails();
            getNodeValue(xpath, clonedNode,"TITLE").ifPresent(userDetails::setTitle);
            getNodeValue(xpath, clonedNode,"FIRST_NAME").ifPresent(userDetails::setFirstName);
            getNodeValue(xpath, clonedNode,"LAST_NAME").ifPresent(userDetails::setLastName);
            getNodeValue(xpath, clonedNode,"FULL_NAME").ifPresent(userDetails::setFullName);
            getNodeValue(xpath, clonedNode,"CONTACT_EMAIL_ADDRESS").ifPresent(userDetails::setContactEmailAddress);
            getNodeValue(xpath, clonedNode,"CONTACT_PHONE_NUMBER").ifPresent(userDetails::setContactPhoneNumber);
            getNodeValue(xpath, clonedNode,"ACCOUNT_TYPE").ifPresent(userDetails::setAccountType);
            getNodeValue(xpath, clonedNode,"ACCOUNT_STATUS").ifPresent(userDetails::setAccountStatus);
            return userDetails;
          } else {
            throw new SpireUserDetailsParserException(String.format("Unexpected element found while parsing the SOAP response body: %s", clonedNode.getNodeName()));
          }
        })
        .collect(Collectors.toList());
    if (userDetailsList.size() != 1) {
      throw new SpireUserDetailsParserException(String.format("Unexpected number of USER_DETAILS found while parsing the SOAP response body, expected 1 but got: %d", userDetailsList.size()));
    } else {
      return userDetailsList.get(0);
    }
  }
}
