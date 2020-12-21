package client.util;

import java.io.StringWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.w3c.dom.Document;

public class TokenEncoder {

	public static String encode(SecurityToken token) throws Exception {
		Document doc = token.getToken().getOwnerDocument();

		// convert EncryptedAssertion to String
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String samlTokenAsString = writer.getBuffer().toString();
		
		// base64 encode the string
		String base64EncodedSamlToken = Base64.encodeBase64String(samlTokenAsString.getBytes(Charset.forName("UTF-8")));
		
		// URLEncode and return
		return URLEncoder.encode(base64EncodedSamlToken, "UTF-8");
	}
}
