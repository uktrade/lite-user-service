package uk.gov.bis.lite.user.spire.spireuserdetails;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingXPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class SpireUserDetailsTestUtils {
  public static final String TITLE = "mr";
  public static final String FIRST_NAME = "applicant1";
  public static final String LAST_NAME = "test";
  public static final String FULL_NAME = "mr applicant1 test";
  public static final String CONTACT_EMAIL_ADDRESS = "applicant1@test.com";
  public static final String CONTACT_PHONE_NUMBER = "+44 (0)208 123 4567";
  public static final String ACCOUNT_TYPE = "EXPORTER";
  public static final String ACCOUNT_STATUS = "ACTIVE";

  private SpireUserDetailsTestUtils(){}

  public static void stubForBody(String body) {
    stubFor(post(urlEqualTo("/spire/fox/ispire/SPIRE_USER_DETAILS"))
        .withBasicAuth("username", "password")
        .withRequestBody(matchingXPath("//SOAP-ENV:Envelope/SOAP-ENV:Body/USER_DETAILS")
            .withXPathNamespace("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/")
        )
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "text/xml; charset=utf-8")
            .withBody(body)));
  }
}
