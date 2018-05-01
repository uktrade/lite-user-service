package uk.gov.bis.lite.user.api.view;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum AccountType {
  EXPORTER,
  REGULATOR,
  UNKNOWN;

  public static Optional<AccountType> getEnumByValue(String value) {
    return Arrays.stream(AccountType.values())
        .filter(e -> StringUtils.equals(value, e.name()))
        .findAny();
  }
}
