package uk.gov.bis.lite.user.api.view;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
  ADMIN,
  SUBMITTER,
  PREPARER;

  public static Optional<Role> getEnumByValue(String value) {
    return Arrays.stream(Role.values())
        .filter(e -> StringUtils.equals(value, e.name()))
        .findAny();
  }
}
