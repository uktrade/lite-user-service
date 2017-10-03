package uk.gov.bis.lite.user.spire;

import org.apache.commons.lang3.StringUtils;
import uk.gov.bis.lite.user.api.CustomerView;
import uk.gov.bis.lite.user.api.Role;
import uk.gov.bis.lite.user.api.SiteView;
import uk.gov.bis.lite.user.api.UserPrivilegesView;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpireUserRolesAdapter {

  private static Map<Role, Integer> rolePriorityMap;

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

  public static UserPrivilegesView adapt(List<SpireUserRole> spireUserRoles) {
    Map<String, CustomerView> customerMap = new HashMap<>();
    Map<String, SiteView> siteMap = new HashMap<>();

    for (SpireUserRole sur: spireUserRoles) {
      if (StringUtils.equals(sur.getResType(), "SPIRE_SAR_USERS")) {
        String customerId = sur.getSarRef();
        Optional<Role> role = spireRoleMapper(sur.getRoleName(), SpireListType.CUSTOMER);
        if (role.isPresent() && !StringUtils.isEmpty(customerId)) {
          if (customerMap.containsKey(customerId)) {
            CustomerView customer = customerMap.get(customerId);
            if (rolePriorityMap.get(role.get()) > rolePriorityMap.getOrDefault(customer.getRole(), -1)){
              customerMap.replace(customerId, new CustomerView(customerId, role.get()));
            }
          } else {
            customerMap.put(customerId, new CustomerView(customerId, role.get()));
          }
        }
      } else if (StringUtils.equals(sur.getResType(), "SPIRE_SITE_USERS")) {
        String siteId = sur.getSiteRef();
        Optional<Role> role = spireRoleMapper(sur.getRoleName(), SpireListType.SITE);
        if (role.isPresent() && !StringUtils.isEmpty(siteId)) {
          if (siteMap.containsKey(siteId)) {
            SiteView site = siteMap.get(siteId);
            if (rolePriorityMap.get(role.get()) > rolePriorityMap.getOrDefault(site.getRole(), -1)){
              siteMap.replace(siteId, new SiteView(siteId, role.get()));
            }
          } else {
            siteMap.put(siteId, new SiteView(siteId, role.get()));
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

    return UserPrivilegesView.builder()
        .setCustomers(customers)
        .setSites(sites)
        .build();
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
