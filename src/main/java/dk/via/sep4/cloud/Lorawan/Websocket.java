package dk.via.sep4.cloud.Lorawan;

import dk.via.sep4.cloud.Persistance.SensorReading;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.HexFormat;
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

            String dataValueString = dataValue.substring(2,6);

            int dataInt = Integer.parseInt(dataValueString,16);

            double dataDouble = ((double) dataInt )/10;

            System.out.println("Double: " + dataDouble);
            SensorReading sensorReading = new SensorReading(dataDouble);
        }else{
            System.out.println("Command value: " + commandValue);
            System.out.println("Intended: " + indented);
        }



        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    };


}