package signature.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;

import signature.client.callback.ClientCallbackHandler;
import signature.client.crypto.UserCertificateStore;
import signature.client.spring.ApplicationContextProvider;
import signature.client.sts.DigstSTSClient;

public class RunButtonHandler implements ActionListener {
	private UI ui;

	public RunButtonHandler(UI ui) {
		this.ui = ui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ui.writeLog("Invoked service");

		KeyStore keystore = getKeystore();
		if (keystore != null) {
			try {
				// set the password on the callback handler
				ClientCallbackHandler.setPassword(ui.getPassword());
				
				// set the alias on the STSClient
				String alias = keystore.aliases().nextElement();
				DigstSTSClient stsClient = ApplicationContextProvider.getApplicationContext().getBean(DigstSTSClient.class);
				stsClient.setAlias(alias);
				
				// set the keystore on the custom Merlin class
				UserCertificateStore.setKs(keystore);

				// first call will also call the STS
				hello("John");
	
				// second call will reuse the cached token we got from the first call
				hello("Jane");
			}
			catch (KeyStoreException ex) {
				ui.writeLog("Failed to retrieve key from keystore: ", ex);
			}
		}
	}
	
	private KeyStore getKeystore() {
		KeyStore ks = null;

		try {
			ks = KeyStore.getInstance("PKCS12");
			ks.load(new FileInputStream(ui.getFile()), ui.getPassword().toCharArray());
		}
		catch (GeneralSecurityException | IOException ex) {
			ui.writeLog("Could not load keystore with provided password", ex);
		}

		return ks;
	}
	
	private void hello(String name) {
		String resp = ui.getPort().helloWorld(name);

		ui.writeLog("Response from service: " + resp);
	}
}
