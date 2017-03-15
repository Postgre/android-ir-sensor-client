package es.alvaroweb.ircamerareader.wscameraview;

import android.util.Log;

import java.util.Iterator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


/**
 * Copyright (C) 2016 Alvaro Bolanos Rodriguez
 */

public class WebsocketConnection extends WebSocketListener {
    private static final String DEBUG_TAG = WebsocketConnection.class.getSimpleName();
    private final WebSocket webSocket;
    private final Request requestToServer;
    private OnReceiveRow callback;
    public WebsocketConnection(String serverURI, OnReceiveRow callback) {
        OkHttpClient client = new OkHttpClient();
        requestToServer = new Request.Builder().url(serverURI).build();
        webSocket = client.newWebSocket(requestToServer, this);
        this.callback = callback;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        log("onOpen: " + response.message());
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        log("onMessage: " + text);

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
        log("onMessage: " + bytes.size() + "bytes received");
        log("onMessage: " + bytes.hex());
        log("onMessage: " + bytes.asByteBuffer());
        callback.receiveRows(bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
        log("onClosing: " + reason + ", code:" + code);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        log("onClosed: " + reason + ", code:" + code);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        Log.e(DEBUG_TAG, "onFailure: " + t.getMessage());
        t.printStackTrace();

    }

    public void send(byte[] bytes){
        ByteString byteString = ByteString.of(bytes);
        webSocket.send(byteString);
    }

    public void close(){
        webSocket.close(1000, "fulfilled");
    }

    private void log(String s){
        Log.d(DEBUG_TAG, s);
    }

    interface OnReceiveRow{
        void receiveRows(ByteString bytes);
    }
}
