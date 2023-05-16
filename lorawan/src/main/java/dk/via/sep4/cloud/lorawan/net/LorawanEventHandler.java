package dk.via.sep4.cloud.lorawan.net;

import org.json.JSONObject;

public interface LorawanEventHandler {
    void uplinkMessage(JSONObject jsonData); //rx
    void downLinkMessageSent(JSONObject jsonData); //gw
    void downLinkMessageAcknowledgement(JSONObject jsonData); //tx
    void downLinkMessageConfirmation(JSONObject jsonData); //txd
}
