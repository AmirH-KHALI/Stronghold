package Stronghold.Network;

import Stronghold.*;

import java.net.InetAddress;
import java.util.Map;

public class ServerPlayer {
    String playerName;
    InetAddress address;
    int port;

    int golds;
    int woods;
    int foods;

    public ServerPlayer (String playerName, InetAddress address, int port) {
        this.playerName = playerName;
        this.address = address;
        this.port = port;

        golds = Integer.parseInt(((Map) ResourceManager.getJson("JSON-GAME").get("initial_resources")).get("gold").toString());
        foods = Integer.parseInt(((Map) ResourceManager.getJson("JSON-GAME").get("initial_resources")).get("food").toString());
        woods = Integer.parseInt(((Map) ResourceManager.getJson("JSON-GAME").get("initial_resources")).get("wood").toString());
    }
}
