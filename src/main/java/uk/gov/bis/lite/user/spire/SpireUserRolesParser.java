package uk.gov.bis.lite.user.spire;

import com.google.common.base.Stopwatch;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import uk.gov.bis.lite.common.spire.client.SpireResponse;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;
import uk.gov.bis.lite.user.spire.SpireUserRole.SpireUserRoleBuilder;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SpireUserRolesParser implements SpireParser<List<SpireUserRole>> {
  private static final Logger LOGGER = LoggerFactory.getLogger(SpireUserRolesParser.class);

  @Override
  public List<SpireUserRole> parseResponse(SpireResponse spireResponse) {
    Stopwatch stopwatch = Stopwatch.createStarted();
    List<SpireUserRole> userRoles = doResponseParsing(spireResponse);
    stopwatch.stop();
    LOGGER.info("The unmarshalling of SpireResponse produced List<SpireUserRole> of size {} and took {}ms,",
        userRoles.size(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
    return userRoles;
  }

  private List<SpireUserRole> doResponseParsing(SpireResponse spireResponse) {
    return spireResponse.getElementChildNodesForList("//ROLE_LIST")
        .parallelStream()
        .map(node -> {
          Node clonedNode = node.cloneNode(true); // Better XPath performance with large DOMs
          if (StringUtils.equals(clonedNode.getNodeName(), "ROLE")) {
            SpireUserRoleBuilder userRoleBuilder = SpireUserRole.builder();
            SpireResponse.getNodeValue(clonedNode,"RES_TYPE").ifPresent(userRoleBuilder::setResType);
            SpireResponse.getNodeValue(clonedNode,"ROLE_NAME").ifPresent(userRoleBuilder::setRoleName);
            SpireResponse.getNodeValue(clonedNode,"FULL_NAME").ifPresent(userRoleBuilder::setFullName);
            SpireResponse.getNodeValue(clonedNode,"SAR_REF").ifPresent(userRoleBuilder::setSarRef);
            SpireResponse.getNodeValue(clonedNode,"SITE_REF").ifPresent(userRoleBuilder::setSiteRef);
            SpireResponse.getNodeValue(clonedNode,"IS_ADMIN").ifPresent(userRoleBuilder::setIsAdmin);
            SpireResponse.getNodeValue(clonedNode,"IS_APPLICANT").ifPresent(userRoleBuilder::setIsApplicant);
            return userRoleBuilder.build();
          } else {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
