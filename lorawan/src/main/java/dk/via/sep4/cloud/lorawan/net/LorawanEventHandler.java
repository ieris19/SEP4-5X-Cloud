package dk.via.sep4.cloud.lorawan.net;

import org.json.JSONObject;
/**
 * This interface is used to handle the events from the lorawan server.
 */
public interface LorawanEventHandler {
    /**
     * This method is used to handle the uplink message event.
     * This is indicated by the cmd value - rx
     * Upon receiving such value, the method will be called to process the payload.
     * @param jsonData The json data received from the lorawan server.
     */
    void uplinkMessage(JSONObject jsonData); //rx
    /**
     * This method is used to handle the downlink message event.
     * This is indicated by the cmd value - gw
     * Upon receiving such value, the method will be called to reply.
     * @param jsonData The json data received from the lorawan server.
     */
    void downLinkMessageSent(JSONObject jsonData); //gw
    /**
     * This method is used to handle the downlink message event.
     * This is indicated by the cmd value - tx
     * Upon receiving such value, the method will be called as an acknowledgement.
     * @param jsonData The json data received from the lorawan server.
     */
    void downLinkMessageAcknowledgement(JSONObject jsonData); //tx
    /**
     * This method is used to handle the downlink message event.
     * This is indicated by the cmd value - txd
     * Upon receiving such value, the method will be called as a confirmation.
     * @param jsonData The json data received from the lorawan server.
     */
    void downLinkMessageConfirmation(JSONObject jsonData); //txd
}
