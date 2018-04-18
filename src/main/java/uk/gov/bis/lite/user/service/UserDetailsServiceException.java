package uk.gov.bis.lite.user.service;

public class UserDetailsServiceException extends RuntimeException {
  public UserDetailsServiceException(String message) {
    super(message);
  }

  public UserDetailsServiceException(String message, Throwable cause) {
    super(message, cause);
  }
}
