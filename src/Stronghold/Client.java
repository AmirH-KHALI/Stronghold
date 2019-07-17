package Stronghold;


import Stronghold.GameObjects.GameAnimation;
import Stronghold.Map.GameMap;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class Client implements Runnable {

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private ArrayList<GameEvent> events;

    private String usrName;

    private Game game;
    private Stage stage;

    public Client (String usrName, String serverIP, Stage stage) {

        events = new ArrayList<>();
        this.usrName = usrName;
        this.stage = stage;

        //initial socket
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //convert server address
        try {
            serverAddress = InetAddress.getByName(serverIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        //client start
        new Thread(this).start();

        //handle event thread
        new Thread(() -> {
            while (true) {
                if (hasNewEvent()) {
                    handleGameEvent(getEvent());
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean hasNewEvent() { return (events.size() > 0); }

    public GameEvent getEvent () {
        GameEvent ge = events.get(0);
        events.remove(0);
        return ge;
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

                events.add( GameEvent.parseFromString(packet) );

                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(String body, InetAddress address, int port) {

        //convert JSON to datagram packet
        DatagramPacket dp = new DatagramPacket(body.getBytes(), body.getBytes().length, address, port);

        //send packet
        try {
            socket.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGameEvent(GameEvent gameEvent) {
        switch (gameEvent.type) {
            case GameEvent.USER_JOINED_TO_NETWORK: {
                String username = gameEvent.message;
                GameGui.addUserToUsersList(username);
                break;
            }
            case GameEvent.START_GAME: {

                new AnimationTimer() {

                    @Override
                    public void handle(long now) {
                        game = new Game(gameEvent.message);
                        try {
                            game.render(stage);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }.start();
                break;
            }
        }
    }

    public void sendGameEvent (int type, String message) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("message", message);
        sendPacket(jsonObject.toJSONString(), serverAddress, 8888);

    }

    public String getUsrName () {
        return usrName;
    }

}
