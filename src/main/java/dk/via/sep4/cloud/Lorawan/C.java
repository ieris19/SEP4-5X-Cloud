package dk.via.sep4.cloud.Lorawan;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.via.sep4.cloud.Persistance.SensorReading;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.mqtt.Client;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

public class C {
    public static void main(String[] args) throws Exception {
        String url = "wss://iotnet.teracom.dk/app?token=vnoUeAAAABFpb3RuZXQudGVyYWNvbS5kawhxYha6idspsvrlQ4C7KWA=";
        String clientId = MqttClient.generateClientId();
        String appkey = "E0597BF885F1F18CF896B91F8E211814";
        String joinEUI = "49B360EEE16A8D4C";
        String EUI_dev = "0004A30B0021B92F";

        String uri = "";
        System.out.println(uri.isEmpty());

        System.out.println(url);

        // Set up SSL context with the default truststore
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init((KeyStore) null);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        sslContext.init(null, trustManagers, null);

        Websocket websocket = new Websocket(url);

        while (true) {
        }
    }
}
