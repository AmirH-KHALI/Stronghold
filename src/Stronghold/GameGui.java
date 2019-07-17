package Stronghold;

import Stronghold.Gui.Buttons.MainMenuButton;
import Stronghold.Gui.Buttons.MainMenuTextField;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Scanner;


public class GameGui extends Application {

    private Scanner in = new Scanner(System.in);

    private Group guiRoot = new Group();

    private static VBox mainBox;
    private static VBox serverPageBox;
    private static VBox clientPageBox;
    private static VBox usersListViewPageBox;

    private static Scene initialScene;
    private static Stage primaryStage;

    private static ListView<String> usersListView;
    private static ArrayList<String> players;

    private static Server myServer;
    private static Client myClient;

    private static MainMenuButton btnStart;

    public static void createMainBox () {


        //create buttons
        MainMenuButton btnCreateServer = new MainMenuButton("GUI-CREATE_SERVER");
        MainMenuButton btnJoin = new MainMenuButton("GUI-JOIN");
        MainMenuButton btnAbout = new MainMenuButton("GUI-ABOUT");
        MainMenuButton btnExit = new MainMenuButton("GUI-EXIT");


        //how buttons works
        btnJoin.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        gotoClientPage();
                        //Game game = new Game("MAP-SAMPLE");
                        //game.render(primaryStage);

                    }
                });

        btnCreateServer.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        gotoServerPage();

                    }
                });

        btnExit.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        primaryStage.close();
                    }
                });


        //add buttons to VBox
        VBox mainMenuBtnBox = new VBox(btnCreateServer, btnJoin, btnAbout, btnExit);
        mainMenuBtnBox.setTranslateX(500);
        mainMenuBtnBox.setTranslateY(500);
        mainMenuBtnBox.setSpacing(30);

        mainBox = new VBox(mainMenuBtnBox);

    }

    public static void createServerPageBox () {

        //create buttons
        MainMenuTextField tfClientName = new MainMenuTextField("Enter Your Name");
        MainMenuButton btnEnter = new MainMenuButton("GUI-CREATE_SERVER");
        MainMenuButton btnBack = new MainMenuButton("GUI-BACK");

        //how buttons works
        btnEnter.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        myServer = new Server("MAP-SAMPLE");

                        myClient = new Client(tfClientName.getText(), "localhost", primaryStage);

                        myClient.sendGameEvent(GameEvent.JOIN_TO_GAME, myClient.getUsrName());

                        gotoUsersListViewPage(true);
                    }
                });

        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        gotoMainMenuPage();
                    }
                });

        //add buttons to VBox
        VBox mainMenuBtnBox = new VBox(tfClientName, btnEnter, btnBack);
        mainMenuBtnBox.setTranslateX(500);
        mainMenuBtnBox.setTranslateY(500);
        mainMenuBtnBox.setSpacing(30);

        serverPageBox = new VBox(mainMenuBtnBox);

    }

    public static void createClientPageBox () {

        //create buttons
        MainMenuTextField tfClientName = new MainMenuTextField("Enter Your Name");
        MainMenuTextField tfServerAddress = new MainMenuTextField("Enter Server Address");
        MainMenuButton btnEnter = new MainMenuButton("GUI-JOIN");
        MainMenuButton btnBack = new MainMenuButton("GUI-BACK");

        //how buttons works
        btnEnter.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        myClient = new Client(tfClientName.getText(), tfServerAddress.getText(), primaryStage);

                        myClient.sendGameEvent(GameEvent.JOIN_TO_GAME, myClient.getUsrName());

                        gotoUsersListViewPage(false);
                    }
                });

        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        gotoMainMenuPage();
                    }
                });

        //add buttons to VBox
        VBox mainMenuBtnBox = new VBox(tfClientName, tfServerAddress, btnEnter, btnBack);
        mainMenuBtnBox.setTranslateX(500);
        mainMenuBtnBox.setTranslateY(500);
        mainMenuBtnBox.setSpacing(30);

        clientPageBox = new VBox(mainMenuBtnBox);

    }

    public static void createUsersListViewPageBox () {

        //create buttons
        btnStart = new MainMenuButton("GUI-START");

        //initial userListView
        usersListView = new ListView<>();
        usersListView.setMaxHeight(136);
        usersListView.setMinHeight(136);
        usersListView.setMaxWidth(361);
        usersListView.setMinWidth(361);

        //how buttons works
        btnStart.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        myServer.startGame();
//                        Game game = new Game("MAP-SAMPLE");
//                        game.render(primaryStage);
                    }
                });


        //add buttons to VBox
        VBox mainMenuBtnBox = new VBox(usersListView, btnStart);
        mainMenuBtnBox.setTranslateX(500);
        mainMenuBtnBox.setTranslateY(500);
        mainMenuBtnBox.setSpacing(30);

        usersListViewPageBox = new VBox(mainMenuBtnBox);

    }

    @Override
    public void start(Stage primaryStage) throws InterruptedException {


        ResourceManager.initialization();

        this.primaryStage = primaryStage;

        primaryStage.setAlwaysOnTop(true);

        //AudioClip theMenuMusic = ResourceManager.getSound("GUI-Music");

        players = new ArrayList<>();

        createMainBox();

        createServerPageBox();

        createClientPageBox();

        createUsersListViewPageBox();

        initialScene = new Scene(mainBox, Stronghold.screenSize.width, Stronghold.screenSize.height);
        initialScene.setCursor(Cursor.DEFAULT);

        primaryStage.setScene(initialScene);
        primaryStage.setTitle("Stronghold");
        primaryStage.setMaxHeight(Stronghold.screenSize.height);
        primaryStage.setMaxWidth(Stronghold.screenSize.width);
        primaryStage.setMinHeight(Stronghold.screenSize.height);
        primaryStage.setMinWidth(Stronghold.screenSize.width);

        //set Background
        mainBox.setBackground(ResourceManager.getBackground("GUI-BACKGROUND"));
        serverPageBox.setBackground(ResourceManager.getBackground("GUI-BACKGROUND"));
        clientPageBox.setBackground(ResourceManager.getBackground("GUI-BACKGROUND"));
        usersListViewPageBox.setBackground(ResourceManager.getBackground("GUI-BACKGROUND"));


        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue != null) primaryStage.setFullScreen(true);
            }

        });

        //theMenuMusic.play();

        primaryStage.show();

        //theMenuMusic.stop();

    }

    public static Scene getInitialScene() {

        return initialScene;

    }

    public static void gotoClientPage() {

        initialScene.setRoot(clientPageBox);

    }

    public static void gotoServerPage() {

        initialScene.setRoot(serverPageBox);
    }

    public static void gotoUsersListViewPage(boolean startBtnVisible) {

        btnStart.setVisible(startBtnVisible);

        initialScene.setRoot(usersListViewPageBox);
    }

    public static void gotoMainMenuPage() {

        initialScene.setRoot(mainBox);
    }

    public static void addUserToUsersList (String name) {

        players.add(name);
        //System.out.println(players);
        usersListView.setItems(FXCollections.observableList(players));
    }

    public static void main(String[] args) { launch(args); }
}
