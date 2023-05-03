package dk.via.sep4.cloud.lorawan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.via.sep4.cloud.persistance.SensorReading;
import org.eclipse.paho.client.mqttv3.*;

public class Connect {

    public static void main(String[] args) throws MqttException {

        String url = "wss://iotnet.cibicom.dk/app";
        String clientId = MqttClient.generateClientId();
        String token = "vnoUeAAAABFpb3RuZXQudGVyYWNvbS5kawhxYha6idspsvrlQ4C7KWA=";
        String appkey = "E0597BF885F1F18CF896B91F8E211814";
        String joinEUI = "49B360EEE16A8D4C";
        String EUI_dev = "0004A30B0021B92F";

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(token);

        MqttClient client = new MqttClient(url, clientId);
        client.connect(options);

        String topic = "application/" + joinEUI + "/device/" + EUI_dev + "/rx";
        client.subscribe(topic);

        client.setCallback(new MqttCallback() {
            public void connectionLost(Throwable cause) {}
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                byte[] payload = message.getPayload();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(payload);
                double temperature = rootNode.get("temperature").asDouble();
                System.out.println("Temperature: " + temperature);
                SensorReading reading = new SensorReading(temperature);
            }
            public void deliveryComplete(IMqttDeliveryToken token) {}
        });




        while(true){

        }



    }
}
