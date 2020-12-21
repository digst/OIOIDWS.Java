package client;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import client.ui.UI;

public class Application {

	public static void main(String[] args) {
    	// This is a hack to support the self-signed SSL certificate used on the WSP
    	// in a real production setting, the service would be protected by a trusted SSL certificate
    	// and setting a custom truststore like this would not be needed
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/ssl-trust.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);

				UI.createAndShowGUI();
			}
		});
	}
}
