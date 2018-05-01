package uk.gov.bis.lite.user.api.view;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum AccountStatus {
  ACTIVE,
  BLOCKED,
  CANCELLED,
  NEW,
  SUSPENDED;

  public static Optional<AccountStatus> getEnumByValue(String value) {
    return Arrays.stream(AccountStatus.values())
        .filter(e -> StringUtils.equals(value, e.name()))
        .findAny();
  }
}
