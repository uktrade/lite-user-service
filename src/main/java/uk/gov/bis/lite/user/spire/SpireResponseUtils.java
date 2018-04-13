package uk.gov.bis.lite.user.spire;

import org.w3c.dom.Node;
import uk.gov.bis.lite.common.spire.client.exception.SpireClientException;

import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

public class SpireResponseUtils {
  private SpireResponseUtils(){}

  public static Optional<String> getNodeValue(XPath xpath, Node node, String name){
    try {
      return Optional.ofNullable(((Node) xpath.evaluate(name, node, XPathConstants.NODE))).map(Node::getTextContent);
    } catch (XPathExpressionException e) {
      throw new SpireClientException("Error occurred while parsing the SOAP response body", e);
    }
  }
}
