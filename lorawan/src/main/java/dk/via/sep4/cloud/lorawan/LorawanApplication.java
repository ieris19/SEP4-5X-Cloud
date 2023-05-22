package dk.via.sep4.cloud.lorawan;

import com.ieris19.lib.files.config.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication(scanBasePackages = {"dk.via.sep4.cloud.lorawan", "dk.via.sep4.cloud.data"})
public class LorawanApplication {
    public static void main(String[] args) {
        sslContextSetup();
        SpringApplication.run(LorawanApplication.class, args);
    }

    protected static void sslContextSetup() {
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

    @Bean
    protected URI lorawanURI() {
        try (FileProperties secret = FileProperties.getInstance("secrets")) {
            return URI.create(secret.getProperty("lorawan.url"));
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("At least one of the accessed properties in secrets.properties doesn't exist", e);
        } catch (IOException e) {
            throw new IllegalStateException("Error reading secrets.properties", e);
        }
    }
}
