package service.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.xml.XMLFault;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;

public class UnderstandFrameworkHeaderInterceptor extends AbstractSoapInterceptor {
	private static final QName frameworkQName = new QName("urn:liberty:sb", "Framework", "sbf");

	public UnderstandFrameworkHeaderInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		Header framework = message.getHeader(frameworkQName);

		// extremely simple validation of the headers presence
		if (framework == null) {
			throw new XMLFault("Missing framework header");
		}
	}
	
	@Override
	public Set<QName> getUnderstoodHeaders() {
		Set<QName> set = new HashSet<>();
		
		set.add(frameworkQName);
		
		return set;
	}
}
