package uk.gov.bis.lite.user.spire.spireuserroles;

import static uk.gov.bis.lite.user.spire.SpireResponseUtils.getNodeValue;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import uk.gov.bis.lite.common.spire.client.SpireResponse;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

public class SpireUserRolesParser implements SpireParser<SpireUserRoles> {
  private static final Logger LOGGER = LoggerFactory.getLogger(SpireUserRolesParser.class);

  @Override
  public SpireUserRoles parseResponse(SpireResponse spireResponse) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    SpireUserRoles spireUserRoles = doResponseParsing(spireResponse);
    stopwatch.stop();
    LOGGER.info("The unmarshalling of SpireResponse produced List<SpireUserRole> of size {} and took {}ms,",
        spireUserRoles.getUserRoles().size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    return spireUserRoles;
  }

  private SpireUserRoles doResponseParsing(SpireResponse spireResponse) {
    XPath xpath = XPathFactory.newInstance().newXPath();
    List<SpireUserRole> userRoles = spireResponse.getElementChildNodesForList("//ROLE_LIST")
        .stream()
        .map(node -> {
          Node clonedNode = node.cloneNode(true); // Better XPath performance with large DOMs
          if (StringUtils.equals(clonedNode.getNodeName(), "ROLE")) {
            SpireUserRole userRole = new SpireUserRole();
            getNodeValue(xpath, clonedNode,"RES_TYPE").ifPresent(userRole::setResType);
            getNodeValue(xpath, clonedNode,"ROLE_NAME").ifPresent(userRole::setRoleName);
            getNodeValue(xpath, clonedNode,"FULL_NAME").ifPresent(userRole::setFullName);
            getNodeValue(xpath, clonedNode,"SAR_REF").ifPresent(userRole::setSarRef);
            getNodeValue(xpath, clonedNode,"SITE_REF").ifPresent(userRole::setSiteRef);
            getNodeValue(xpath, clonedNode,"IS_ADMIN").ifPresent(userRole::setIsAdmin);
            getNodeValue(xpath, clonedNode,"IS_APPLICANT").ifPresent(userRole::setIsApplicant);
            return userRole;
          } else {
            LOGGER.warn("Unexpected element found while parsing the SOAP response body: \"{}\"", clonedNode.getNodeName());
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    return new SpireUserRoles(userRoles);
  }
}
