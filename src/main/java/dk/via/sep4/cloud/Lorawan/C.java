package dk.via.sep4.cloud.Lorawan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.via.sep4.cloud.Persistance.SensorReading;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.thethingsnetwork.data.common.Connection;
import org.thethingsnetwork.data.common.messages.ActivationMessage;
import org.thethingsnetwork.data.common.messages.DataMessage;
import org.thethingsnetwork.data.mqtt.Client;

import java.io.IOException;

public class C {
    public static void main(String[] args) throws Exception {
        String url = "wss://iotnet.cibicom.dk/app?token=vnoUeAAAABFpb3RuZXQudGVyYWNvbS5kawhxYha6idspsvrlQ4C7KWA=";
        String uri = "";
        String clientId = MqttClient.generateClientId();
        String appkey = "E0597BF885F1F18CF896B91F8E211814";
        String joinEUI = "49B360EEE16A8D4C";
        String EUI_dev = "0004A30B0021B92F";
        MqttConnectOptions options = new MqttConnectOptions();
        Client client = new Client(url, clientId, appkey, options);

        client.onActivation((String _devId, ActivationMessage _data) -> System.out.println("Activation: " + _devId + ", data: " + _data));
        client.onError((Throwable _error) -> System.err.println("error: " + _error.getMessage()));
        client.onConnected((Connection _client) -> System.out.println("connected !"));
        client.onMessage((String devId, DataMessage data) -> {
            String payload = data.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = null;
            try {
                rootNode = objectMapper.readTree(payload);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            double temperature = rootNode.get("temperature").asDouble();
            System.out.println("Temperature: " + temperature);
            SensorReading reading = new SensorReading(temperature);
        });

        client.start();
        while (true) {
        }
    }
}
