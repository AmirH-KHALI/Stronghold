package Stronghold;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Server implements Runnable {

    private DatagramSocket socket;

    //private Game game;
    private String mapName;

    private final int port = 8888;

    public Server (String mapName) {

        //game = new Game(mapName);

        this.mapName = mapName;

        //initial socket
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //server start
        new Thread(this).start();

        //game update thread
        new Thread(() -> {
            while (!socket.isClosed()) {
                updateMapObjects();
                sendMapObjectsToAll();
                sendResourcesForAll();
                try {
                    Thread.sleep(1000 / 4);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void updateMapObjects () {

    }

    public void sendMapObjectsToAll () {

    }

    public void sendResourcesForAll () {

    }

    @Override
    public void run () {
        try {

            //initial buffer
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

            while (true) {

                //receive data and cast to String
                socket.receive(incoming);
                byte[] data = incoming.getData();
                String packet = new String(data, 0, incoming.getLength());

                analyzePacket(packet, incoming.getAddress(), incoming.getPort());

                Thread.sleep(50);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void analyzePacket(String body, InetAddress address, int port) {

        GameEvent gameEvent = GameEvent.parseFromString(body);

        switch (gameEvent.type) {
            case GameEvent.JOIN_TO_GAME: {

                String username = gameEvent.message;

                //Send previous players for new player
                for (ServerPlayer player : Game.players) {
                    GameEvent joinGameEvent = new GameEvent(GameEvent.USER_JOINED_TO_NETWORK, player.playerName);
                    sendPacket(joinGameEvent.getJSON(), address, port);
                }

                Game.players.add(new ServerPlayer(username, address, port));

                //create building

                //Send join alert for all clients
                GameEvent joinGameEvent = new GameEvent(GameEvent.USER_JOINED_TO_NETWORK, username);
                sendPacketForAll(joinGameEvent.getJSON());
                break;
            }
        }
    }

    public boolean sendPacket(String body, InetAddress address, int port) {

        //convert JSON to datagram packet
        DatagramPacket dp = new DatagramPacket(body.getBytes(), body.getBytes().length, address, port);

        //send packet
        try {
            socket.send(dp);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendPacketForAll(String body) {

        for (ServerPlayer player : Game.players) {
            sendPacket(body, player.address, player.port);
        }

    }

    public void startGame () {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", GameEvent.START_GAME);
        jsonObject.put("message", mapName);

        sendPacketForAll(jsonObject.toJSONString());
    }

}
