package dk.via.sep4.cloud.lorawan.websocket;

import dk.via.sep4.cloud.data.dto.SensorReading;
import dk.via.sep4.cloud.lorawan.websocket.utils.DataHandler;
import dk.via.sep4.cloud.lorawan.websocket.utils.WebSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This class is used to implement a client for the lorawan server.
 * It implements the LorawanEventHandler interface to handle the events received from the server.
 */
@Slf4j
@Component
public class WebsocketHandler implements LorawanEventHandler {
    private final LorawanSocketListener listener;
    private final LorawanSocketDispatcher dispatcher;
    private final DataHandler dataHandler;
    private boolean isOpen;
    private WebSocket webSocket;

    public WebsocketHandler(@Autowired DataHandler handler) {
        this.isOpen = false;
        this.listener = new LorawanSocketListener();
        this.dispatcher = new LorawanSocketDispatcher();
        this.dataHandler = handler;
        onRefresh();
    }

    @Override
    public void onUpLink(JSONObject jsonData) {
        log.info("Received up-link message from device.");
        SensorReading reading = dataHandler.parsePayload(jsonData);
        if (reading != null) {
            dataHandler.save(reading);
        }
    }

    @Override
    public void onGatewayStatus(JSONObject jsonData) {
        log.info("Received gateway status message");
        log.trace("Gateway status: {}", jsonData);
        JSONObject payload = dataHandler.createPayload(jsonData);
        if (payload != null) {
            dispatcher.sendText(payload);
        } else {
            log.error("Could not create payload for down-link message");
        }
    }

    @Override
    public void onDownLink(JSONObject jsonData) {
        log.info("Received acknowledgement of down-link message");
        log.trace("Acknowledgement: {}", jsonData);
    }

    @Override
    public void onDownLinkConfirmation(JSONObject jsonData) {
        log.info("Received confirmation of down-link message");
        log.trace("Confirmation: {}", jsonData);
    }

    @Override
    public void unknownCommandReceived(JSONObject dataJson) {
        log.error("Unknown Lorawan command received");
        if (log.isDebugEnabled()) {
            String command = dataJson.getString("cmd");
            log.debug("Command value: {}", command);
            log.debug("Contents: {}", dataJson.toString(4));
        }
    }

    @Override
    public void onRefresh() {
        if (!isOpen) {
            log.info("Refreshing WebSocket connection");
            this.webSocket = WebSocketFactory.createWebSocket(this.listener);
        } else {
            this.webSocket.request(1);
        }
    }

    @Override
    public boolean isListening() {
        return isOpen;
    }

    /**
     * This class is used to listen for messages from the lorawan server.
     * The methods in this class are invoked by the WebSocket when a message is received.
     */
    private class LorawanSocketListener implements WebSocket.Listener {
        @Override
        public void onOpen(WebSocket webSocket) {
            webSocket.request(1);
            WebsocketHandler.this.isOpen = true;
            log.info("Successfully opened WebSocket for requests");
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            log.error("A {} exception was thrown", error.getClass().getSimpleName());
            if (error.getMessage() != null) {
                log.debug("Message: {}", error.getMessage());
            }
            log.trace("Detailed: ", error);
            WebsocketHandler.this.isOpen = false;
            // Abort the WebSocket connection to prevent processing further messages in a broken state
            webSocket.abort();
            log.error("Websocket abruptly closed");
        }

        @Override
        public CompletionStage<Void> onClose(WebSocket webSocket, int statusCode, String reason) {
            log.error("WebSocket closing gracefully");
            log.debug("Status:{} Reason: {}", statusCode, reason);
            /*
             The WebSocket connection is not explicitly closed here
             By not requesting any more messages, the connection will naturally end
             */
            WebsocketHandler.this.isOpen = false;
            return CompletableFuture.completedFuture("Socket gracefully closed").thenAccept(log::trace);
        }

        @Override
        public CompletionStage<Void> onText(WebSocket webSocket, CharSequence data, boolean last) {
            log.trace("Received message");
            JSONObject dataJson = new JSONObject(data.toString());
            switch (dataJson.getString("cmd")) {
                case "rx" -> WebsocketHandler.this.onUpLink(dataJson);
                case "gw" -> WebsocketHandler.this.onGatewayStatus(dataJson);
                case "tx" -> WebsocketHandler.this.onDownLink(dataJson);
                case "txd" -> WebsocketHandler.this.onDownLinkConfirmation(dataJson);
                default -> WebsocketHandler.this.unknownCommandReceived(dataJson);
            }
            webSocket.request(1);
            return CompletableFuture.completedFuture("Message processed").thenAccept(log::trace);
        }
    }

    /**
     * This class is used to dispatch data to the WebSocket server.
     */
    private class LorawanSocketDispatcher {
        public void sendText(JSONObject payload) {
            log.trace("Sending limits to server: {}", payload.toString(4));
            WebsocketHandler.this.webSocket.sendText(payload.toString(), true);
        }
    }
}
