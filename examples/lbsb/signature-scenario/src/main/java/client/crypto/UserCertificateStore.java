package client.crypto;

import java.io.IOException;
import java.security.KeyStore;
import java.util.Properties;

import org.apache.wss4j.common.crypto.Merlin;
import org.apache.wss4j.common.crypto.PasswordEncryptor;
import org.apache.wss4j.common.ext.WSSecurityException;

public class UserCertificateStore extends Merlin {
	private static KeyStore ks;

	public UserCertificateStore(Properties properties, ClassLoader loader, PasswordEncryptor passwordEncryptor) throws WSSecurityException, IOException {
		super(properties, loader, passwordEncryptor);

		// the UserCertificateStore instance is created on-demand by the CXF framework, meaning when it is about to call the STS,
		// so as long as we ensure that a user-supplied keystore is configured before calling the STS, this will work as intended
		this.setKeyStore(UserCertificateStore.ks);
	}

	public static void setKs(KeyStore keyStore) {
		ks = keyStore;
	}
}
