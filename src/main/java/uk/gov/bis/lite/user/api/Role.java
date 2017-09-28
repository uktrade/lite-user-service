package uk.gov.bis.lite.user.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
  ADMIN,
  SUBMITTER,
  PREPARER;

  @JsonCreator
  public static Role fromJsonValue(String value) {
    return getEnumByValue(value).orElse(null);
  }

  @JsonValue
  public String getValue() {
    return this.name();
  }

  public static Optional<Role> getEnumByValue(String value) {
    return Arrays.stream(Role.values())
        .filter(e -> StringUtils.equals(value, e.getValue()))
        .findAny();
  }
}
