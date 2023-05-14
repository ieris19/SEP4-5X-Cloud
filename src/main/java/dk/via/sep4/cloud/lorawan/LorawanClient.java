package dk.via.sep4.cloud.lorawan;

import com.ieris19.lib.files.config.FileProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Service
public class LorawanClient implements CommandLineRunner {
	public void run(String[] args) {
		sslContextSetup();
		LorawanSocketListener websocket = new LorawanSocketListener(retrieveSecretURL());
	}

	private void sslContextSetup() {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
					TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init((KeyStore) null);
			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			sslContext.init(null, trustManagers, null);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Defined SSL algorithm not found", e);
		} catch (KeyStoreException e) {
			throw new RuntimeException("Failed to initialize trust managers", e);
		} catch (KeyManagementException e) {
			throw new RuntimeException("Failed to initialize SSL context", e);
		}
	}

	private String retrieveSecretURL() {
		try (FileProperties secret = FileProperties.getInstance("secrets")) {
			return secret.getProperty("lorawan.url");
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("At least one of the accessed properties in secrets.properties doesn't exist", e);
		} catch (IOException e) {
			throw new IllegalStateException("Error reading secrets.properties", e);
		}
	}
}
