package uk.gov.bis.lite.user.config.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.PrincipalImpl;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, PrincipalImpl> {
  private String login;
  private String password;

  public SimpleAuthenticator(String login, String password) {
    this.login = login;
    this.password = password;
  }

  @Override
  public java.util.Optional<PrincipalImpl> authenticate(BasicCredentials credentials) throws AuthenticationException {
    if (password.equals(credentials.getPassword()) && login.equals(credentials.getUsername())) {
      return Optional.of(new PrincipalImpl(credentials.getUsername()));
    }
    return Optional.empty();
  }
}
