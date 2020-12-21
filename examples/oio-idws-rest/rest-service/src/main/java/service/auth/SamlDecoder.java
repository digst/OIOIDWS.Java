package service.auth;

import java.io.ByteArrayInputStream;
import java.security.Key;
import java.security.KeyStore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Component
public class SamlDecoder {
	
	@Autowired
	private KeyStore keyStore;

	public SamlAssertionWrapper decodeAsertion(String samlToken) throws Exception {
		Element tokenElement = decodeToken(samlToken);

		String alias = keyStore.aliases().nextElement();
		Key privateKey = keyStore.getKey(alias, "Test1234".toCharArray());

		Document decryptedElementDOM = decryptElementDOM(tokenElement.getOwnerDocument(), privateKey);
		Element assertion = (Element) decryptedElementDOM.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion").item(0);

		return new SamlAssertionWrapper(assertion);
	}

	private static Element decodeToken(String token) throws Exception {
		byte[] decodedToken = Base64.decodeBase64(token);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new ByteArrayInputStream(decodedToken));
		
		return (Element) doc.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptedData").item(0);
	}

	private static Document decryptElementDOM(Document doc, Key rsaKey) throws Exception {
		Element ee = (Element) doc.getElementsByTagNameNS("http://www.w3.org/2001/04/xmlenc#", "EncryptedData").item(0);

		XMLCipher cipher = XMLCipher.getInstance();
		cipher.init(XMLCipher.DECRYPT_MODE, null);
		EncryptedData encryptedData = cipher.loadEncryptedData(doc, ee);

		KeyInfo ki = encryptedData.getKeyInfo();
		EncryptedKey encryptedKey = ki.itemEncryptedKey(0);

		XMLCipher cipher2 = XMLCipher.getInstance();
		cipher2.init(XMLCipher.UNWRAP_MODE, rsaKey);
		Key key = cipher2.decryptKey(encryptedKey, encryptedData.getEncryptionMethod().getAlgorithm());

		cipher.init(XMLCipher.DECRYPT_MODE, key);

		return cipher.doFinal(doc, ee);
	}
}
