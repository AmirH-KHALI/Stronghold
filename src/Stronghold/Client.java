package Stronghold;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Client implements Runnable {

    private DatagramSocket socket;
    private InetAddress serverAddress;

    private String usrName;

    public Client (String usrName, String serverIP) {

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

                //do what do you want

                System.out.println(packet);

                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(String body, InetAddress address, int port) {

        DatagramPacket dp = new DatagramPacket(body.getBytes(), body.getBytes().length, address, port);

        try {
            socket.send(dp);
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    public String readStringFromJSON (String pathToJSON) throws Exception {
        FileReader reader = new FileReader(pathToJSON);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
        return jsonObject.toJSONString();
    }

    public void stringWriteInJSON (String command, Path pathToJSON) {
        try {
            Files.write(pathToJSON, command.getBytes());
        } catch (Exception e) {

        }
    }



    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Enter : ");
        Client myClient = new Client(input.next(), "localhost");
        try {
            System.out.println("Enter : ");
            myClient.sendPacket(input.next(), InetAddress.getByName("localhost"), 8888);
        } catch (Exception e) {

        }
    }
}
