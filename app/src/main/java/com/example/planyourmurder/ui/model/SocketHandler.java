package com.example.planyourmurder.ui.model;

import okhttp3.WebSocket;

public class SocketHandler {
    private static WebSocket socket;

    public static synchronized WebSocket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(WebSocket socket){
        SocketHandler.socket = socket;
    }
}