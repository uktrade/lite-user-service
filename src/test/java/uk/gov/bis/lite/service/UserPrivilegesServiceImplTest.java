package uk.gov.bis.lite.service;

import org.junit.Test;
import uk.gov.bis.lite.user.service.UserPrivilegesService;
import uk.gov.bis.lite.user.service.UserPrivilegesServiceImpl;

public class UserPrivilegesServiceImplTest {

  @Test
  public void dummyTest() throws Exception {
    UserPrivilegesService service = new UserPrivilegesServiceImpl();
    service.getUserPrivileges("1");
  }
}
