package Stronghold;

import Stronghold.Gui.Buttons.MainMenuButton;
import Stronghold.Gui.Buttons.MainMenuTextField;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.util.Scanner;


public class GameGui extends Application {

    private Scanner in = new Scanner(System.in);

    private Group guiRoot = new Group();
    private static VBox mainBox;
    private static VBox serverPageBox;
    private static VBox clientPageBox;
    private static Scene initialScene;
    private static Stage primaryStage;

    public static void createMainBox () {

        MainMenuButton btnCreateServer = new MainMenuButton("GUI-CREATE_SERVER");
        MainMenuButton btnJoin = new MainMenuButton("GUI-JOIN");
        MainMenuButton btnAbout = new MainMenuButton("GUI-ABOUT");
        MainMenuButton btnExit = new MainMenuButton("GUI-EXIT");

        btnJoin.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        gotoClientPage();

                        //theMenuMusic.stop();

                    }
                });

        btnCreateServer.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        gotoServerPage();

                        //theMenuMusic.stop();

                    }
                });

        btnExit.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        primaryStage.close();
                    }
                });

        VBox mainMenuBtnBox = new VBox(btnCreateServer, btnJoin, btnAbout, btnExit);
        mainMenuBtnBox.setTranslateX(500);
        mainMenuBtnBox.setTranslateY(500);
        mainMenuBtnBox.setSpacing(30);

        mainBox = new VBox(mainMenuBtnBox);

    }

    public static void createServerPageBox () {

        MainMenuTextField tfClientName = new MainMenuTextField("Enter Your Name");
        MainMenuButton btnEnter = new MainMenuButton("GUI-CREATE_SERVER");
        MainMenuButton btnBack = new MainMenuButton("GUI-BACK");

        btnEnter.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Server myServer = new Server("sample");
                        Client myClient = new Client(tfClientName.getText(), "localhost");
                    }
                });

        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        gotoMainMenuPage();
                    }
                });

        VBox mainMenuBtnBox = new VBox(tfClientName, btnEnter, btnBack);
        mainMenuBtnBox.setTranslateX(500);
        mainMenuBtnBox.setTranslateY(500);
        mainMenuBtnBox.setSpacing(30);

        serverPageBox = new VBox(mainMenuBtnBox);

    }

    public static void createClientPageBox () {

        MainMenuTextField tfClientName = new MainMenuTextField("Enter Your Name");
        MainMenuTextField tfServerAddress = new MainMenuTextField("Enter Server Address");
        MainMenuButton btnEnter = new MainMenuButton("GUI-JOIN");
        MainMenuButton btnBack = new MainMenuButton("GUI-BACK");

        btnEnter.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Game myGame = new Game("sample");
                        myGame.render(primaryStage);
                    }
                });

        btnBack.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        gotoMainMenuPage();
                    }
                });

        VBox mainMenuBtnBox = new VBox(tfClientName, tfServerAddress, btnEnter, btnBack);
        mainMenuBtnBox.setTranslateX(500);
        mainMenuBtnBox.setTranslateY(500);
        mainMenuBtnBox.setSpacing(30);

        clientPageBox = new VBox(mainMenuBtnBox);

    }

    @Override
    public void start(Stage primaryStage) {


        ResourceManager.initialization();

        this.primaryStage = primaryStage;

        primaryStage.setAlwaysOnTop(true);

        //AudioClip theMenuMusic = ResourceManager.getSound("GUI-Music");

        createMainBox();

        createServerPageBox();

        createClientPageBox();

        initialScene = new Scene(mainBox, Stronghold.screenSize.width, Stronghold.screenSize.height);
        initialScene.setCursor(Cursor.DEFAULT);

        primaryStage.setScene(initialScene);
        primaryStage.setTitle("Stronghold");
        primaryStage.setMaxHeight(Stronghold.screenSize.height);
        primaryStage.setMaxWidth(Stronghold.screenSize.width);
        primaryStage.setMinHeight(Stronghold.screenSize.height);
        primaryStage.setMinWidth(Stronghold.screenSize.width);

        mainBox.setBackground(ResourceManager.getBackground("GUI-BACKGROUND"));
        serverPageBox.setBackground(ResourceManager.getBackground("GUI-BACKGROUND"));
        clientPageBox.setBackground(ResourceManager.getBackground("GUI-BACKGROUND"));


        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue != null)
                    primaryStage.setFullScreen(true);
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

        //Game myGame = new Game("sample");

        //myGame.render(primaryStage);

        //mainBox.setVisible(false);
    }

    public static void gotoServerPage() {

        initialScene.setRoot(serverPageBox);
    }

    public static void gotoMainMenuPage() {

        initialScene.setRoot(mainBox);
    }

    public static void main(String[] args) {

        launch(args);

    }

}