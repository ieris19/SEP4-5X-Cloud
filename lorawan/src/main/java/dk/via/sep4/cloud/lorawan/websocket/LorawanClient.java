package dk.via.sep4.cloud.lorawan.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LorawanClient {
    private final LorawanEventHandler lorawanHandler;

    @Autowired
    public LorawanClient(@Autowired LorawanEventHandler handler) {
        this.lorawanHandler = handler;
    }

    public boolean refreshConnection() {
        lorawanHandler.onRefresh();
        return lorawanHandler.isListening();
    }
}


