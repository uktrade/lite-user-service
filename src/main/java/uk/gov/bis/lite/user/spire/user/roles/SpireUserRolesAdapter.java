package uk.gov.bis.lite.user.spire.user.roles;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import uk.gov.bis.lite.user.api.view.CustomerView;
import uk.gov.bis.lite.user.api.view.SiteView;
import uk.gov.bis.lite.user.api.view.UserPrivilegesView;
import uk.gov.bis.lite.user.api.view.enums.Role;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpireUserRolesAdapter {

  private static final Map<Role, Integer> ROLE_PRIORITY_MAP;

  static {
    ROLE_PRIORITY_MAP = new EnumMap<>(Role.class);
    ROLE_PRIORITY_MAP.put(Role.ADMIN, 2);
    ROLE_PRIORITY_MAP.put(Role.SUBMITTER, 1);
    ROLE_PRIORITY_MAP.put(Role.PREPARER, 0);
  }

  public static UserPrivilegesView adapt(SpireUserRoles spireUserRoles) {
    Map<String, CustomerView> customerMap = new HashMap<>();
    Map<String, SiteView> siteMap = new HashMap<>();

    for (SpireUserRole sur : spireUserRoles.getUserRoles()) {
      if (StringUtils.equals(sur.getResType(), "SPIRE_SAR_USERS")) {
        String customerId = sur.getSarRef();
        Optional<Role> role = mapSpireRole(sur.getRoleName());
        if (role.isPresent() && !StringUtils.isEmpty(customerId)) {
          CustomerView customer = new CustomerView()
              .setCustomerId(customerId)
              .setRole(role.get());

          customerMap.merge(customerId, customer, (existingView, newView) -> {
            int existingRolePriority = ROLE_PRIORITY_MAP.getOrDefault(existingView.getRole(), -1);
            int newRolePriority = ROLE_PRIORITY_MAP.get(newView.getRole());
            return newRolePriority > existingRolePriority ? newView : existingView;
          });
        }
      } else if (StringUtils.equals(sur.getResType(), "SPIRE_SITE_USERS")) {
        String siteId = sur.getSiteRef();
        Optional<Role> role = mapSpireRole(sur.getRoleName());
        if (role.isPresent() && !StringUtils.isEmpty(siteId)) {
          SiteView site = new SiteView()
              .setSiteId(siteId)
              .setRole(role.get());

          siteMap.merge(siteId, site, (existingView, newView) -> {
            int existingRolePriority = ROLE_PRIORITY_MAP.getOrDefault(existingView.getRole(), -1);
            int newRolePriority = ROLE_PRIORITY_MAP.get(newView.getRole());
            return newRolePriority > existingRolePriority ? newView : existingView;
          });
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
        .setCustomers(customers)
        .setSites(sites);
  }

  @VisibleForTesting
  static Optional<Role> mapSpireRole(String spireRoleName) {
    if (StringUtils.equals(spireRoleName, "SAR_ADMINISTRATOR") || StringUtils.equals(spireRoleName, "SITE_ADMINISTRATOR")) {
      return Optional.of(Role.ADMIN);
    } else if (StringUtils.equals(spireRoleName, "APPLICATION_SUBMITTER")) {
      return Optional.of(Role.SUBMITTER);
    } else if (StringUtils.equals(spireRoleName, "APPLICATION_PREPARER")) {
      return Optional.of(Role.PREPARER);
    } else {
      return Optional.empty();
    }
  }
}
