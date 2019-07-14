package Stronghold;

import javafx.stage.Stage;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Server implements Runnable {
    private Thread sendToAll;
    private DatagramSocket socket;
    private Game myGame;

    public Server (String mapName) {

        //myGame = new Game(mapName, this);

        //myGame.render(stage);

        try {
            socket = new DatagramSocket(7777);
        } catch (SocketException e) {
            //System.out.println("socket not created!");
        }

        Thread listenTread = new Thread(this);
        listenTread.start();

        sendToAll = new Thread(() -> {
           while (true) {
               updateMapObjects();
               sendMapObjectsToAll();
               sendResourcesForAll();
               try {
                   Thread.sleep(1000 / 4);
               } catch (InterruptedException e) {
                   //System.out.println("!!");
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

    }
}
