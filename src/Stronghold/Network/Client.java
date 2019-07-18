package Stronghold.Network;


import Stronghold.*;
import Stronghold.GameObjects.Building.Barracks;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

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
                Client client = this;
                AnimationTimer x = new AnimationTimer() {

                    @Override
                    public void handle(long now) {
                        game = new Game(client, usrName, gameEvent.message);
                        try {
                            game.render(stage);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                };
                x.start();
                //x.stop();
                break;
            }
            case GameEvent.DISPLAY_BUILDING: {
                //System.out.println(">>>>>>>>>>>>>>>>>>>> catch by client");
                AnimationTimer x = new AnimationTimer() {

                    @Override
                    public void handle(long now) {
                        int[] location = new int[2];
                        String name = gameEvent.message.substring(0, gameEvent.message.indexOf("@"));
                        String buildingName = gameEvent.message.substring(gameEvent.message.indexOf("@") + 1, gameEvent.message.indexOf(":"));
                        location[0] = Integer.parseInt(gameEvent.message.substring(gameEvent.message.indexOf(":") + 1, gameEvent.message.indexOf(",")));
                        location[1] = Integer.parseInt(gameEvent.message.substring(gameEvent.message.indexOf(",") + 1));
                        game.buildBuilding(name, buildingName, location[0], location[1]);
                    }

                };
                x.start();
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
