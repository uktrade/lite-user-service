package uk.gov.bis.lite.user.api.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum AccountStatus {
  ACTIVE,
  BLOCKED,
  CANCELLED,
  NEW,
  SUSPENDED;

  @JsonCreator
  public static AccountStatus fromJsonValue(String value) {
    return getEnumByValue(value).orElse(null);
  }

  @JsonValue
  public String getValue() {
    return this.name();
  }

  public static Optional<AccountStatus> getEnumByValue(String value) {
    return Arrays.stream(AccountStatus.values())
        .filter(e -> StringUtils.equals(value, e.getValue()))
        .findAny();
  }
}
