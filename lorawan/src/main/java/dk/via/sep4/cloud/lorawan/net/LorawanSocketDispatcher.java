package dk.via.sep4.cloud.lorawan.net;

import org.json.JSONObject;

import java.net.http.WebSocket;
/**
 * This class is used to dispatch the data received from the lorawan server to the client.
 */
public class LorawanSocketDispatcher {
    private final WebSocket server;
    private final LorawanClient client;

    public LorawanSocketDispatcher(WebSocket server, LorawanClient client) {
        this.server = server;
        this.client = client;
    }

    public void sendLimits(JSONObject jsonData) {
        String payload = client.getDataHandler().createLimitsPayload(jsonData);
        server.sendText(payload, true);
    }
}