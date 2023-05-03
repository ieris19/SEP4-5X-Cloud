package dk.via.sep4.cloud.lorawan;

import dk.via.sep4.cloud.persistance.SensorReading;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CompletableFuture;

public class Websocket implements WebSocket.Listener {
    private WebSocket server = null;

    // Send down-link message to device
    // Must be in Json format according to https://github.com/ihavn/IoT_Semester_project/blob/master/LORA_NETWORK_SERVER.md
    public void sendDownLink(String jsonTelegram) {
        server.sendText(jsonTelegram,true);
    }

    // E.g. url: "wss://iotnet.teracom.dk/app?token=??????????????????????????????????????????????="
    // Substitute ????????????????? with the token you have been given
    public Websocket(String url) {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create(url), this);

        server = ws.join();
    }

    //onOpen()
    public void onOpen(WebSocket webSocket) {
        // This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
        webSocket.request(1);
        System.out.println("WebSocket Listener has been opened for requests.");
    }

    //onError()
    public void onError​(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());
        webSocket.abort();
    };
    //onClose()
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);
        return new CompletableFuture().completedFuture("onClose() completed.").thenAccept(System.out::println);
    };
    //onPing()
    public CompletionStage<?> onPing​(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Ping: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Ping completed.").thenAccept(System.out::println);
    };
    //onPong()
    public CompletionStage<?> onPong​(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Pong: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Pong completed.").thenAccept(System.out::println);
    };
    //onText()
    public CompletionStage<?> onText​(WebSocket webSocket, CharSequence data, boolean last) {
        System.out.println("OnText entered");
        String indented = (new JSONObject(data.toString())).toString(4);


        JSONObject dataJson = new JSONObject(data.toString());
        String dataValue = dataJson.getString("data");
        String commandValue = dataJson.getString("cmd");

        if(commandValue.equals("rx")){
            byte[] dataBytes = dataValue.getBytes();
            //substrings of the payload data string
            String temperatureValueString = dataValue.substring(2,6);

//            String humidityValueString = dataValue.substring(6,7);
//            String co2ValueString = dataValue.substring(8,11);
//            String soundValueString = dataValue.substring(12,15);
//            String lightValueString = dataValue.substring(16,19);
            //to int from string
            short temperatureInt = (short) Integer.parseInt(temperatureValueString,16);

//            int humidityInt = Integer.parseInt(humidityValueString,16);
//            int co2Int = Integer.parseInt(co2ValueString,16);
//            int soundInt = Integer.parseInt(soundValueString,16);
//            int lightInt = Integer.parseInt(lightValueString,16);
            //to double from int
            double temperatureDouble = ((double) temperatureInt )/10;

//            double humidityDouble = ((double) humidityInt )/10;
//            double co2Double = ((double) co2Int )/10;
//            double soundDouble = ((double) soundInt )/10;
//            double lightDouble = ((double) lightInt )/10;
            //print double values
            System.out.println("Temperature Double: " + temperatureDouble);

//            System.out.println("Humidity Double: " + humidityDouble);
//            System.out.println("CO2 Double: " + co2Double);
//            System.out.println("Sound Double: " + soundDouble);
//            System.out.println("Light Double: " + lightDouble);
            //log readings
            SensorReading sensorReadingTemperature = new SensorReading(temperatureDouble);

        }else{
            //System.out.println("Command value: " + commandValue);
            //System.out.println("Intended: " + indented);
        }



        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    };


}