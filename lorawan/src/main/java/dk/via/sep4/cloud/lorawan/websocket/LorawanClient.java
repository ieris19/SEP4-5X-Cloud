package dk.via.sep4.cloud.lorawan.websocket;

import dk.via.sep4.cloud.data.DataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service @Slf4j
public class LorawanClient {
    private final WebsocketHandler websocketHandler;

    @Autowired
    public LorawanClient(DataRepository dataRepository, URI lorawanURI) {
        DataHandler dataHandler = new DataHandler(dataRepository);
        websocketHandler = new WebsocketHandler(lorawanURI, dataHandler);
    }

    public boolean refreshConnection() {
        websocketHandler.refresh();
        return websocketHandler.isOpen();
    }

    @Scheduled(fixedRate = 3 * 60 * 1000)
    public void printStatus() {
        log.info("Connection status -> {}", websocketHandler.isOpen() ? "open" : "closed");
    }
}
