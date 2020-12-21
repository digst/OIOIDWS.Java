package service.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class SbfFrameworkHeader {
	@XmlAttribute(name="version")
	private final String version = "2.0";
	
	@XmlAttribute(name="profile", namespace="urn:liberty:sb:profile")
	private final String profile = "urn:liberty:sb:profile:basic";
	
	public String getVersion() {
		return version;
	}
	
	public String getProfile() {
		return profile;
	}
}
