package uk.gov.bis.lite.user.spire;

import org.apache.commons.lang3.StringUtils;
import uk.gov.bis.lite.common.spire.client.SpireResponse;
import uk.gov.bis.lite.common.spire.client.parser.SpireParser;
import uk.gov.bis.lite.user.spire.SpireUserRole.SpireUserRoleBuilder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SpireUserRolesParser implements SpireParser<List<SpireUserRole>> {
  @Override
  public List<SpireUserRole> parseResponse(SpireResponse spireResponse) {
    return spireResponse.getElementChildNodesForList("//ROLE_LIST")
        .stream()
        .filter(node -> !StringUtils.equals(node.getNodeName(), "ERROR"))
        .map(node -> {
          if (StringUtils.equals(node.getNodeName(), "ROLE")) {
            SpireUserRoleBuilder userRoleBuilder = SpireUserRole.builder();
            SpireResponse.getNodeValue(node,"RES_TYPE").ifPresent(userRoleBuilder::setResType);
            SpireResponse.getNodeValue(node,"ROLE_NAME").ifPresent(userRoleBuilder::setRoleName);
            SpireResponse.getNodeValue(node,"FULL_NAME").ifPresent(userRoleBuilder::setFullName);
            SpireResponse.getNodeValue(node,"SAR_REF").ifPresent(userRoleBuilder::setSarRef);
            SpireResponse.getNodeValue(node,"SITE_REF").ifPresent(userRoleBuilder::setSiteRef);
            SpireResponse.getNodeValue(node,"IS_ADMIN").ifPresent(userRoleBuilder::setIsAdmin);
            SpireResponse.getNodeValue(node,"IS_APPLICANT").ifPresent(userRoleBuilder::setIsApplicant);
            return userRoleBuilder.build();
          } else {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
