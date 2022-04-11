package com.example.pong;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class defines Nicolas and Stephen's version of the classic PONG GAME
 * CS2040 Final Project
 */
public class PongV2 extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Declares the 3 panes, one for each screen
        Pane p1 = new Pane();
        Pane p2 = new Pane();
        Pane p3 = new Pane();

        //Declare the 3 screens of the game
        Scene welcomeScreen = new Scene(p1, WIDTH, HEIGHT);
        Scene gameScreen = new Scene(p2, WIDTH, HEIGHT);
        Scene overScreen = new Scene(p3, WIDTH, HEIGHT);

        //WELCOME SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //create the window widgets of the WELCOME SCREEN
        Text welcome = new Text(WIDTH / 2 - 100, HEIGHT / 3, "Welcome to PONG!");
        welcome.setFont(Font.font("Verdana", 20));

        Button start = new Button("Start Game");
        start.setLayoutX(WIDTH / 2 - 40);
        start.setLayoutY(HEIGHT / 2);
        start.setScaleX(1.5);
        start.setScaleY(1.5);

        Text instructions = new Text(WIDTH / 2 - 40, HEIGHT - 100, "Instructions");

        // define the pane hierarchy for the WELCOME SCREEN
        p1.getChildren().addAll(welcome, start, instructions);

        //EVENT Listeners for WELCOME SCREEN
        start.setOnMouseClicked(e -> {
            primaryStage.setScene(gameScreen);
        });

        //GAME SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //create the window widgets of the GAME SCREEN
        Rectangle humanPaddle = new Rectangle(20, 20, 15, 150);

        Rectangle computerPaddle = new Rectangle(550, 20, 150, 15);
        Circle ball = new Circle(500, 500, 15);
        Button quit = new Button("QUIT");
        quit.setLayoutX(WIDTH / 2 - 40);
        quit.setLayoutY(HEIGHT / 2);

        // defines the pane hierarchy for the WELCOME SCREEN
        p2.getChildren().addAll(humanPaddle, computerPaddle, ball, quit);

        //EVENT Listeners for GAME SCREEN
        quit.setOnMouseClicked(e -> {
            primaryStage.setScene(overScreen);
        });

        //GAME OVER SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //create the window widgets of the GAME OVER SCREEN
        Button quit2 = new Button("QUIT");
        quit2.setLayoutX(WIDTH / 2 - 40);
        quit2.setLayoutY(HEIGHT / 2);
        Button restart = new Button("RESTART");
        Text gameOver = new Text("GAME OVER !");

        //Defines the pane hierarchy for the GAME OVER SCREEN
        p3.getChildren().addAll(quit2, restart, gameOver);

        //Event listeners for GAME OVER SCREEN
        quit2.setOnMouseClicked(e -> {
            primaryStage.setScene(welcomeScreen);
        });
        restart.setOnMouseClicked(e -> {
            primaryStage.setScene(gameScreen);
        });

       // --------------------------------------------------------------------------------------------------------------------------------------
        //shows the window of the application, begining with the WELCOME SCREEN
        primaryStage.setTitle("P O N G");
        primaryStage.setScene(welcomeScreen);
        primaryStage.show();
    }
}
