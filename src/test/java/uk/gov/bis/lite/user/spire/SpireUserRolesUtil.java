package uk.gov.bis.lite.user.spire;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import uk.gov.bis.lite.common.spire.client.SpireResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

public class SpireUserRolesUtil {
  public static String RES_TYPE_SPIRE_SAR_USERS = "SPIRE_SAR_USERS";
  public static String RES_TYPE_SPIRE_SITE_USERS = "SPIRE_SITE_USERS";
  public static String ROLE_SAR_ADMINISTRATOR = "SAR_ADMINISTRATOR";
  public static String ROLE_SITE_ADMINISTRATOR = "SITE_ADMINISTRATOR";
  public static String ROLE_APPLICATION_SUBMITTER = "APPLICATION_SUBMITTER";
  public static String ROLE_APPLICATION_PREPARER = "APPLICATION_PREPARER";
  public static String FULL_NAME = "Mr Test";

  public static SpireUserRole buildCustomerAdmin(String sarRef) {
    return new SpireUserRole()
        .setResType(RES_TYPE_SPIRE_SAR_USERS)
        .setSarRef(sarRef)
        .setRoleName(ROLE_SAR_ADMINISTRATOR)
        .setIsAdmin("Y")
        .setIsApplicant("N")
        .setFullName(FULL_NAME);
  }

  public static SpireUserRole buildCustomerSubmitter(String sarRef) {
    return new SpireUserRole()
        .setResType(RES_TYPE_SPIRE_SAR_USERS)
        .setSarRef(sarRef)
        .setRoleName(ROLE_APPLICATION_SUBMITTER)
        .setIsAdmin("N")
        .setIsApplicant("Y")
        .setFullName(FULL_NAME);
  }

  public static SpireUserRole buildCustomerPreparer(String sarRef) {
    return new SpireUserRole()
        .setResType(RES_TYPE_SPIRE_SAR_USERS)
        .setSarRef(sarRef)
        .setRoleName(ROLE_APPLICATION_PREPARER)
        .setIsAdmin("N")
        .setIsApplicant("Y")
        .setFullName(FULL_NAME);
  }

  public static SpireUserRole buildSiteAdmin(String siteRef) {
    return new SpireUserRole()
        .setResType(RES_TYPE_SPIRE_SITE_USERS)
        .setSiteRef(siteRef)
        .setRoleName(ROLE_SITE_ADMINISTRATOR)
        .setIsAdmin("Y")
        .setIsApplicant("N")
        .setFullName(FULL_NAME);
  }

  public static SpireUserRole buildSiteSubmitter(String siteRef) {
    return new SpireUserRole()
        .setResType(RES_TYPE_SPIRE_SITE_USERS)
        .setSiteRef(siteRef)
        .setRoleName(ROLE_APPLICATION_SUBMITTER)
        .setIsAdmin("N")
        .setIsApplicant("Y")
        .setFullName(FULL_NAME);
  }

  public static SpireUserRole buildSitePreparer(String siteRef) {
    return new SpireUserRole()
        .setResType(RES_TYPE_SPIRE_SITE_USERS)
        .setSiteRef(siteRef)
        .setRoleName(ROLE_APPLICATION_PREPARER)
        .setIsAdmin("N")
        .setIsApplicant("Y")
        .setFullName(FULL_NAME);
  }

  public static void stubForBody(String body) {
    stubFor(post(urlEqualTo("/spire/fox/ispire/SPIRE_USER_ROLES"))
        .withBasicAuth("username", "password")
        .withRequestBody(matchingXPath("//SOAP-ENV:Envelope/SOAP-ENV:Body/spir:getRoles/userId")
            .withXPathNamespace("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/")
            .withXPathNamespace("spir", "http://www.fivium.co.uk/fox/webservices/ispire/SPIRE_USER_ROLES")
        )
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "text/xml; charset=utf-8")
            .withBody(body)));
  }

  public static SpireResponse createSpireResponse(String soapMessageString) throws Exception {
    InputStream is = new ByteArrayInputStream(soapMessageString.getBytes());
    SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, is);
    return new SpireResponse(soapMessage);
  }
}
