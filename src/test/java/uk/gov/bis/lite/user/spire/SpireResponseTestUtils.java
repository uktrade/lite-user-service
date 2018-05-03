package uk.gov.bis.lite.user.spire;

import uk.gov.bis.lite.common.spire.client.SpireResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;

public class SpireResponseTestUtils {
  private SpireResponseTestUtils(){}

  public static SpireResponse createSpireResponse(String soapMessageString) throws Exception {
    InputStream is = new ByteArrayInputStream(soapMessageString.getBytes());
    SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(null, is);
    return new SpireResponse(soapMessage);
  }
}
