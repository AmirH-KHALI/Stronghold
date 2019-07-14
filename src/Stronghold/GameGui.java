package Stronghold;

import Stronghold.Gui.Buttons.MainMenuButton;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
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
    private static Scene initialScene;
    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {


        ResourceManager.initialization();

        this.primaryStage = primaryStage;

        primaryStage.setAlwaysOnTop(true);

        AudioClip theMenuMusic = ResourceManager.getSound("GUI-Music");

        MainMenuButton btnCreateServer = new MainMenuButton("GUI-CREATE_SERVER");
        MainMenuButton btnJoin = new MainMenuButton("GUI-JOIN");
        MainMenuButton btnAbout = new MainMenuButton("GUI-ABOUT");
        MainMenuButton btnExit = new MainMenuButton("GUI-EXIT");

        btnJoin.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        //Game myGame = new Game("sample");

                        gotoClientPage();

                        //theMenuMusic.stop();

                        //myGame.render(primaryStage);

                    }
                });

        btnCreateServer.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        //Game myGame = new Game("sample");

                        gotoServerPage();

                        //Server myServer = new Server("sample", primaryStage);

                        //theMenuMusic.stop();

                        //myGame.render(primaryStage);

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

        initialScene = new Scene(mainBox, Stronghold.screenSize.width, Stronghold.screenSize.height);
        initialScene.setCursor(Cursor.DEFAULT);

        primaryStage.setScene(initialScene);
        primaryStage.setTitle("Stronghold");
        primaryStage.setMaxHeight(Stronghold.screenSize.height);
        primaryStage.setMaxWidth(Stronghold.screenSize.width);
        primaryStage.setMinHeight(Stronghold.screenSize.height);
        primaryStage.setMinWidth(Stronghold.screenSize.width);

        mainBox.setBackground(ResourceManager.getBackground("GUI-BACKGROUND"));


//        primaryStage.setFullScreenExitHint("");
//        primaryStage.setFullScreen(true);
//        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
//
//        primaryStage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
//            @Override
//            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//
//                if(newValue != null)
//                    primaryStage.setFullScreen(true);
//            }
//
//        });


//        new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//
//                primaryStage.setFullScreen(true);
//
//            }
//        }.start();


//        theMenuMusic.play();
//
        primaryStage.show();



        // Should Be Removed
//        new Game("sample").render(primaryStage);
//        theMenuMusic.stop();

    }

    public static Scene getInitialScene() {

        return initialScene;

    }

    public static void gotoClientPage() {

        Game myGame = new Game("sample");

        myGame.render(primaryStage);

        //mainBox.setVisible(false);
    }

    public static void gotoServerPage() {

        //mainBox.setVisible(false);
    }

    public static void main(String[] args) {

        launch(args);

    }

}