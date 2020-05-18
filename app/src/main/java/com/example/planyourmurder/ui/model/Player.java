package com.example.planyourmurder.ui.model;

public class Player {
    private static String name;
    private static int gameId;

    public static synchronized int getGameId() {
        return gameId;
    }

    public static synchronized void setGameId(int gameId) {
        Player.gameId = gameId;
    }

    public static synchronized String getName() {
        return name;
    }

    public static synchronized void setName(String name) {
        Player.name = name;
    }
}
