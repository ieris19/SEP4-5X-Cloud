package dk.via.sep4.cloud.lorawan.net;

import dk.via.sep4.cloud.data.DataRepository;
import lombok.Getter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
/**
 * This class is used to implement a client for the lorawan server.
 * It implements the LorawanEventHandler interface to handle the events received from the server.
 */
@Service
public class LorawanClient implements LorawanEventHandler {
    private final Logger logger = LoggerFactory.getLogger(LorawanClient.class);
    private LorawanSocketListener listener;
    private LorawanSocketDispatcher dispatcher;
    @Getter
    private LorawanDataHandler dataHandler;
    private final WebSocket lorawanSocket;

    @Autowired
    public LorawanClient(DataRepository repository, URI lorawnURI) {
        HttpClient client = HttpClient.newHttpClient();
        this.dataHandler = new LorawanDataHandler(repository);
        this.listener = new LorawanSocketListener(this);
        this.lorawanSocket = client.newWebSocketBuilder().buildAsync(lorawnURI, listener).join();
        this.dispatcher = new LorawanSocketDispatcher(lorawanSocket, this);
    }

    @Scheduled(fixedRate = 180000)
    public void keepAlive() {
        lorawanSocket.sendPing(ByteBuffer.allocate(0));
    }

    @Override
    public void uplinkMessage(JSONObject jsonData) {
        logger.info("Received up-link message from device.");
        dataHandler.parsePayload(jsonData);
    }

    @Override
    public void downLinkMessageSent(JSONObject jsonData) {
        logger.info("Received gateway status message");
        dispatcher.sendLimits(jsonData);
    }

    @Override
    public void downLinkMessageAcknowledgement(JSONObject jsonData) {
        logger.info("Received acknowledgement of down-link message");
    }

    @Override
    public void downLinkMessageConfirmation(JSONObject jsonData) {
        logger.info("Received confirmation of down-link message");
    }
}
