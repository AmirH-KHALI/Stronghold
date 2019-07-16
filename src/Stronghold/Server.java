package Stronghold;

import java.net.*;
import java.util.Scanner;

public class Server implements Runnable {

    private Thread sendMapObjectsThread;
    private DatagramSocket socket;

    public Server (String mapName) {

        try {
            socket = new DatagramSocket(8888);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Thread listenThread = new Thread(this);
        listenThread.start();

        sendMapObjectsThread = new Thread(() -> {
            while (true) {
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

    @Override
    public void run () {

        try {
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            while (true) {
                socket.receive(incoming);
                byte[] data = incoming.getData();
                String packet = new String(data, 0, incoming.getLength());

                //do the thing

                System.out.println(packet);


                Thread.sleep(1000 / 20);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        new Server("sample");
        System.out.println("Enter : ");
        Client myClient = new Client(input.next(), "localhost");
    }
}
