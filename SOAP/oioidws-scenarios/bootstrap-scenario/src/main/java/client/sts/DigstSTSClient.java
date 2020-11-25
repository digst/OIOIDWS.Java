package client.sts;

import java.nio.charset.Charset;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.Bus;
import org.apache.cxf.ws.security.trust.STSClient;
import org.w3c.dom.Element;

import client.interceptor.STSAddressingInterceptor;
import dk.itst.oiosaml.sp.UserAssertion;
import dk.itst.oiosaml.sp.UserAssertionHolder;
import dk.itst.oiosaml.sp.UserAttribute;

public class DigstSTSClient extends STSClient {
	public DigstSTSClient(Bus b) {
		super(b);

		this.out.add(new STSAddressingInterceptor());

		createUniqueContextAttribute();
		
    	// This is a hack to support the self-signed SSL certificate used on the WSP
    	// in a real production setting, the service would be protected by a trusted SSL certificate
    	// and setting a custom truststore like this would not be needed
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
	}

	@Override
	protected void addAppliesTo(XMLStreamWriter writer, String appliesTo) throws XMLStreamException {
		createUniqueContextAttribute();

		if (appliesTo != null && addressingNamespace != null) {
			// modified the namespace so it uses ws-policy 1.1 namespace (not supported by CXF, so we have to do this hacky thing)
			writer.writeStartElement("wsp", "AppliesTo", "http://schemas.xmlsoap.org/ws/2002/12/policy");
			writer.writeNamespace("wsp", "http://schemas.xmlsoap.org/ws/2002/12/policy");
			writer.writeStartElement("wsa", "EndpointReference", addressingNamespace);
			writer.writeNamespace("wsa", addressingNamespace);
			writer.writeStartElement("wsa", "Address", addressingNamespace);
			writer.writeCharacters(appliesTo);
			writer.writeEndElement();
			writer.writeEndElement();
			writer.writeEndElement();
		}
	}

	@Override
	public Element getActAsToken() throws Exception {
		// grab the IdP supplied token from the UserAssertionHolder and copy the relevant attribute into the ActAs field in the STS request
		UserAssertion userAssertion = UserAssertionHolder.get();
		UserAttribute attribute = userAssertion.getAttribute("urn:liberty:disco:2006-08:DiscoveryEPR");
		byte[] rawAssertion = Base64.decodeBase64(attribute.getValue());

		String actAsToken = new String(rawAssertion, Charset.forName("UTF-8"));

		return getDelegationSecurityToken(actAsToken);
	}

	// we are required to set a unique @Context attribute on the request
	private void createUniqueContextAttribute() {
		this.context = "urn:uuid:" + UUID.randomUUID().toString();
	}
}
