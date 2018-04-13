package uk.gov.bis.lite.user.api.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum AccountType {
  EXPORTER,
  CANCELLED,
  REGULATOR;

  @JsonCreator
  public static AccountType fromJsonValue(String value) {
    return getEnumByValue(value).orElse(null);
  }

  @JsonValue
  public String getValue() {
    return this.name();
  }

  public static Optional<AccountType> getEnumByValue(String value) {
    return Arrays.stream(AccountType.values())
        .filter(e -> StringUtils.equals(value, e.getValue()))
        .findAny();
  }
}
