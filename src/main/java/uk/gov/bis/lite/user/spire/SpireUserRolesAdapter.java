package uk.gov.bis.lite.user.spire;

import org.apache.commons.lang3.StringUtils;
import uk.gov.bis.lite.user.api.view.CustomerView;
import uk.gov.bis.lite.user.api.view.Role;
import uk.gov.bis.lite.user.api.view.SiteView;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpireUserRolesAdapter {

  private static final Map<Role, Integer> rolePriorityMap;

  static {
    rolePriorityMap = new EnumMap<>(Role.class);
    rolePriorityMap.put(Role.ADMIN, 2);
    rolePriorityMap.put(Role.SUBMITTER, 1);
    rolePriorityMap.put(Role.PREPARER, 0);
  }

  private enum SpireListType {
    SITE,
    CUSTOMER
  }

  public static UserPrivilegesView adapt(SpireUserRoles spireUserRoles) {
    Map<String, CustomerView> customerMap = new HashMap<>();
    Map<String, SiteView> siteMap = new HashMap<>();

    for (SpireUserRole sur: spireUserRoles.getUserRoles()) {
      if (StringUtils.equals(sur.getResType(), "SPIRE_SAR_USERS")) {
        String customerId = sur.getSarRef();
        Optional<Role> role = spireRoleMapper(sur.getRoleName(), SpireListType.CUSTOMER);
        if (role.isPresent() && !StringUtils.isEmpty(customerId)) {
          if (customerMap.containsKey(customerId)) {
            Role existingRole = customerMap.get(customerId).getRole();
            int existingRolePriority = rolePriorityMap.getOrDefault(existingRole, -1);
            int rolePriority = rolePriorityMap.get(role.get());
            if (rolePriority > existingRolePriority) {
              CustomerView customer = new CustomerView()
                  .setCustomerId(customerId)
                  .setRole(role.get());
              customerMap.replace(customerId, customer);
            }
          } else {
            CustomerView customer = new CustomerView()
                .setCustomerId(customerId)
                .setRole(role.get());
            customerMap.put(customerId, customer);
          }
        }
      } else if (StringUtils.equals(sur.getResType(), "SPIRE_SITE_USERS")) {
        String siteId = sur.getSiteRef();
        Optional<Role> role = spireRoleMapper(sur.getRoleName(), SpireListType.SITE);
        if (role.isPresent() && !StringUtils.isEmpty(siteId)) {
          if (siteMap.containsKey(siteId)) {
            Role existingRole = siteMap.get(siteId).getRole();
            int existingRolePriority = rolePriorityMap.getOrDefault(existingRole, -1);
            int rolePriority = rolePriorityMap.get(role.get());
            if (rolePriority > existingRolePriority){
              SiteView site = new SiteView()
                  .setSiteId(siteId)
                  .setRole(role.get());
              siteMap.replace(siteId, site);
            }
          } else {
            SiteView site = new SiteView()
                .setSiteId(siteId)
                .setRole(role.get());
            siteMap.put(siteId, site);
          }
        }
      }
    }

    List<CustomerView> customers = customerMap.values().stream()
        .sorted(Comparator.comparing(CustomerView::getCustomerId))
        .collect(Collectors.toList());

    List<SiteView> sites = siteMap.values().stream()
        .sorted(Comparator.comparing(SiteView::getSiteId))
        .collect(Collectors.toList());

    return new UserPrivilegesView()
        .setUserAccountType(spireUserRoles.getUserAccountType())
        .setCustomers(customers)
        .setSites(sites);
  }

  private static Optional<Role> spireRoleMapper(String spireRoleName, SpireListType listType) {
    if (StringUtils.isEmpty(spireRoleName)) {
      return Optional.empty();
    } else if (StringUtils.equals(spireRoleName , "SAR_ADMINISTRATOR") && listType == SpireListType.CUSTOMER) {
      return Optional.of(Role.ADMIN);
    } else if (StringUtils.equals(spireRoleName , "SITE_ADMINISTRATOR") && listType == SpireListType.SITE) {
      return Optional.of(Role.ADMIN);
    } else if (StringUtils.equals(spireRoleName , "APPLICATION_SUBMITTER")) {
      return Optional.of(Role.SUBMITTER);
    } else if (StringUtils.equals(spireRoleName , "APPLICATION_PREPARER")) {
      return Optional.of(Role.PREPARER);
    } else {
      return Optional.empty();
    }
  }
}
