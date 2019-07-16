package Stronghold;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Server implements Runnable {

    private DatagramSocket socket;

    private Game game;

    public Server (String mapName) {

        game = new Game("sample");

        try {
            socket = new DatagramSocket(8888);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Thread listenThread = new Thread(this);
        listenThread.start();

        new Thread(() -> {
            while (!socket.isClosed()) {
                updateMapObjects();
                sendMapObjectsToAll();
                sendResourcesForAll();
                try {
                    Thread.sleep(1000 / 4);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        });

    }

    public void updateMapObjects() {

    }

    public void sendMapObjectsToAll () {

    }

    public void sendResourcesForAll () {

    }

    public void stop () {
        socket.close();
    }

    @Override
    public void run () {

        try {
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            while (true) {
                socket.receive(incoming);
                byte[] data = incoming.getData();
                String packet = new String(data, 0, incoming.getLength());

                analyzePacket(packet, incoming.getAddress(), incoming.getPort());

                Thread.sleep(1000 / 20);
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
                for (ServerPlayer player : game.players) {
                    GameEvent joinGameEvent = new GameEvent(GameEvent.USER_JOINED_TO_NETWORK, player.playerName);
                    sendPacket(joinGameEvent.getJSON(), address, port);
                }

                game.players.add(new ServerPlayer(username, address, port));

                //create building

                //Send join alert for all clients
                GameEvent joinGameEvent = new GameEvent(GameEvent.USER_JOINED_TO_NETWORK, username);
                sendPacketForAll(joinGameEvent.getJSON());
                break;
            }
        }
    }

    boolean sendPacket(String body, InetAddress address, int port) {
        DatagramPacket dp = new DatagramPacket(body.getBytes(), body.getBytes().length, address, port);
        try {
            socket.send(dp);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    void sendPacketForAll(String body) {
        for (ServerPlayer player : game.players) {
            sendPacket(body, player.address, player.port);
        }
    }

//    static Scanner input = new Scanner(System.in);
//
//    public static void main(String[] args) {
//        new Server("sample");
//        System.out.println("Enter : ");
//        Client myClient = new Client(input.next(), "localhost");
//    }
}
