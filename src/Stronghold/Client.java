package Stronghold;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public class Client implements Runnable {

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private Queue<GameEvent> events;

    public Client (String serverIP) {
        events = new LinkedList<>();

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {

        }

        try {
            serverAddress = InetAddress.getByName(serverIP);
        } catch (UnknownHostException e) {

        }

        new Thread(this).start();
    }

    @Override
    public void run () {

    }
}
