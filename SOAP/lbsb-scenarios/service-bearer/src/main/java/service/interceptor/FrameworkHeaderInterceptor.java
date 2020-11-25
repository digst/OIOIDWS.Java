package service.interceptor;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.xml.XMLFault;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.phase.Phase;

import service.model.SbfFrameworkHeader;

public class FrameworkHeaderInterceptor extends AbstractSoapInterceptor {
	public FrameworkHeaderInterceptor() {
		super(Phase.PRE_PROTOCOL);
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		List<Header> headers = message.getHeaders();

		try {
			Header framework = new SoapHeader(new QName("urn:liberty:sb", "Framework", "sbf"), new SbfFrameworkHeader(), new JAXBDataBinding(SbfFrameworkHeader.class));
			headers.add(framework);
		}
		catch (Exception ex) {
			throw new XMLFault(ex.getMessage());
		}

		message.put(Header.HEADER_LIST, headers);
	}
}
