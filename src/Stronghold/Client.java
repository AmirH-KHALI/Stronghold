package Stronghold;


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

    public Client (String usrName, String serverIP) {

        events = new ArrayList<>();

        this.usrName = usrName;

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        try {
            serverAddress = InetAddress.getByName(serverIP);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        new Thread(this).start();

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

    public void stop () { socket.close(); }

    @Override
    public void run () {

        try {
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            while (true) {
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

        DatagramPacket dp = new DatagramPacket(body.getBytes(), body.getBytes().length, address, port);

        try {
            //System.out.println("hi");
            socket.send(dp);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public void handleGameEvent(GameEvent gameEvent) {
        switch (gameEvent.type) {
            case GameEvent.USER_JOINED_TO_NETWORK: {
                String username = gameEvent.message;
                GameGui.addUserToUsersList(username);
                break;
            }
        }
    }

    public void addToUsersList () {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", GameEvent.JOIN_TO_GAME);
        jsonObject.put("message", this.usrName);
        sendPacket(jsonObject.toJSONString(), serverAddress, 8888);

    }


//    static Scanner input = new Scanner(System.in);
//
//    public static void main(String[] args) {
//        System.out.println("Enter : ");
//        Client myClient = new Client(input.next(), "localhost");
//        try {
//            System.out.println("Enter : ");
//            myClient.sendPacket(input.next(), InetAddress.getByName("localhost"), 8888);
//        } catch (Exception e) {
//
//        }
//    }
}
