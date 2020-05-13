package com.example.planyourmurder.ui.model;

public class TokenHandler {
    private static String token;

    public static synchronized String getToken(){
        return token;
    }

    public static synchronized void setToken(String token){
        TokenHandler.token = token;
    }
}
