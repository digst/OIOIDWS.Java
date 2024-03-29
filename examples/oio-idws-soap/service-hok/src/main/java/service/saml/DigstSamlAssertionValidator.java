package service.saml;

import org.apache.wss4j.common.ext.WSSecurityException;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.apache.wss4j.dom.handler.RequestData;
import org.apache.wss4j.dom.validate.Credential;
import org.apache.wss4j.dom.validate.SamlAssertionValidator;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.xml.XMLObject;
import service.bpp.BasicPrivilegeProfileDeserializer;
import service.bpp.PrivilegeListType;

import java.util.ArrayList;
import java.util.List;

public class DigstSamlAssertionValidator extends SamlAssertionValidator {
	// Hard-coded expected audience in token. Multiple values can be added if needed
	private List<String> audienceRestrictions = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("https://wsp.oioidws-java.dk");
		}
	};

	@SuppressWarnings("unchecked")
	@Override
	public Credential validate(Credential credential, RequestData data) throws WSSecurityException {
		// Set the valid audiences for this request
		data.setAudienceRestrictions(audienceRestrictions);

		// Perform the actual validation
		Credential validatedCredential = super.validate(credential, data);

		// Extract the Basic Privilege Profile attribute and place it on a ThreadLocal for later use
		SamlAssertionWrapper samlAssertion = credential.getSamlAssertion();
		if (samlAssertion.getSaml2() != null) {
			Assertion saml2 = samlAssertion.getSaml2();

			for (AttributeStatement attributeStatement : saml2.getAttributeStatements()) {
				for (Attribute attribute : attributeStatement.getAttributes()) {
					if ("Privileges".equals(attribute.getFriendlyName())) {
						for (XMLObject attributeValue : attribute.getAttributeValues()) {
							if (!attributeValue.isNil()) {
								String privilege = attributeValue.getDOM().getTextContent();

								try {
									PrivilegeListType privilegeList = BasicPrivilegeProfileDeserializer.deserializeBase64EncodedPrivilegeList(privilege);
									AssertionHolder.set(privilegeList);
								}
								catch (Exception ex) {
									throw new WSSecurityException(WSSecurityException.ErrorCode.FAILURE, ex);
								}
							}
						}
					}
				}
			}
		}

		return validatedCredential;
	}
}
