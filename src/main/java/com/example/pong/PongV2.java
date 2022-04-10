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

public class PongV2 extends Application {
    private static final int WIDTH= 800;
    private static final int HEIGHT = 600;
    @Override
    public void start(Stage primaryStage) throws Exception {
        //WELCOME SCREEN
        //create the window widgets
        Text welcome = new Text(WIDTH/2-100, HEIGHT/3, "Welcome to PONG!");
        welcome.setFont(Font.font("Verdana", 20));

        Button start = new Button("Start Game");
        start.setLayoutX(WIDTH/2-40);
        start.setLayoutY(HEIGHT/2);
        start.setScaleX(1.5);
        start.setScaleY(1.5);

        Text instructions = new Text(WIDTH/2-40,HEIGHT-100,"Instructions");

        //create the panes
        Pane p1 = new Pane();
        Pane p2 = new Pane();
        Pane p3 = new Pane();


        // define the pane hierarchy
        p1.getChildren().addAll(welcome, start, instructions);

        //create a new scene with parent node
        Scene welcomeScreen = new Scene(p1, WIDTH, HEIGHT);
        Scene gameScreen = new Scene(p2, WIDTH, HEIGHT);
        Scene overScreen = new Scene(p3, WIDTH, HEIGHT);

        //GAME SCREEN
        start.setOnMouseClicked(e->{
            primaryStage.setScene(gameScreen);
        });

        Rectangle humanPaddle = new Rectangle (20,20, 15,150);

        Rectangle computerPaddle = new Rectangle (550,20,150,15);
        Circle ball = new Circle(500,500,15);
        Button quit = new Button("QUIT");
        quit.setLayoutX(WIDTH/2-40);
        quit.setLayoutY(HEIGHT/2);
        quit.setOnMouseClicked(e->{
            primaryStage.setScene(overScreen);
        });

        p2.getChildren().addAll(humanPaddle, computerPaddle,ball,quit);

        //GAME OVER SCREEN

        Button quit2 = new Button("QUIT");
        quit2.setLayoutX(WIDTH/2-40);
        quit2.setLayoutY(HEIGHT/2);
        Button restart = new Button("RESTART");
        Text gameOver = new Text("GAME OVER !");

        quit2.setOnMouseClicked(e->{
            primaryStage.setScene(welcomeScreen);
        });
        restart.setOnMouseClicked(e->{
            primaryStage.setScene(gameScreen);
        });

        p3.getChildren().addAll(quit2,restart,gameOver);

        //show the window
        primaryStage.setTitle("P O N G");
        primaryStage.setScene(welcomeScreen);
        primaryStage.show();
    }
}
