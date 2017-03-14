package es.alvaroweb.ircamerareader.wscameraview;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;


/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */

public class WebsocketConnection extends WebSocketClient {
    private static final String DEBUG_TAG = WebsocketConnection.class.getSimpleName();
    private OnReceiveRow callback;
    public WebsocketConnection(String serverURI, OnReceiveRow callback) throws URISyntaxException {
        super(new URI(serverURI));
        this.callback = callback;
    }


    @Override
    public void onOpen(ServerHandshake h) {
        log("request: " + h.getHttpStatus() + ", " + h.getHttpStatusMessage());
    }



    @Override
    public void onMessage(ByteBuffer bytes) {
        log(bytes.toString());
        if(bytes.hasArray()){
            callback.receiveRow(bytes.array());
        }

    }

    @Override
    public void onMessage(String message) {
        log(message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        log(reason + " code:" + code);
    }

//    @Override
//    public void onWebsocketClosing(WebSocket conn, int code, String reason, boolean remote) {
//        super.onWebsocketClosing(conn, code, reason, remote);
//        log(reason + " code:" + code);
//    }

    @Override
    public void onError(Exception ex) {
        Log.e(DEBUG_TAG, ex.getMessage());
    }

    @Override
    public void send(byte[] data) throws NotYetConnectedException {
        // process command or somthing
        super.send(data);
    }

    private void log(String s){
        Log.d(DEBUG_TAG, s);
    }

    interface OnReceiveRow{
        void receiveRow(byte[] bytes);
    }
}
