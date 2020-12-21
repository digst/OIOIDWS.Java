package client.callback;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;

// CXF ignores the password in property file (sometimes), and seems to think it should have the password supplied as a callback ;)
public class ClientCallbackHandler implements CallbackHandler {

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof WSPasswordCallback) {
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[i];
				pc.setPassword("Test1234");
			}
		}
	}
}
