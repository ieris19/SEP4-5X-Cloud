package dk.via.sep4.cloud.Lorawan;

import dk.via.sep4.cloud.Persistance.SensorReading;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.thethingsnetwork.data.common.messages.*;

public class Connect {

    public static void main(String[] args) throws MqttException {

        String url = "wss://iotnet.cibicom.dk/app?";
        String token = "vnoUeAAAABFpb3RuZXQudGVyYWNvbS5kawhxYha6idspsvrlQ4C7KWA=";
        String appkey = "E0597BF885F1F18CF896B91F8E211814";
        String joinEUI = "49B360EEE16A8D4C";
        String EUI_dev = "0004A30B0021B92F";

        MqttClient client = new MqttClient(url, MqttClient.generateClientId());


        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                // Handle connection lost event
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Extract the payload from the MQTT message
                byte[] payload = message.getPayload();
                String data = new String(payload);
                System.out.println("Received data: " + data);
                double temperature = 1;
                SensorReading reading = new SensorReading(temperature);
            }


            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Handle delivery complete event
            }
        });

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(joinEUI);
        options.setPassword(appkey.toCharArray());
        client.connect(options);

        String uplinkTopic = String.format("%s/devices/+/up", joinEUI);
        client.subscribe(uplinkTopic);

        while(true){

        }



    }
}
