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
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_RADIUS = 15;
    private int ballXPos = WIDTH / 2;
    private int ballYPos = WIDTH / 2;
    private int ballYSpeed = 1;
    private int ballXSpeed = 1;
    private boolean gameStarted = false;
    private int playerScore = 0;
    private int playerXPos = 0;
    private double computerXPos = WIDTH - PLAYER_WIDTH;
    private double playerYPos = WIDTH / 2;
    private double computerYPos = WIDTH / 2;

    //Global node declarations
    //create the window widgets of the GAME SCREEN
    Rectangle humanPaddle = new Rectangle(0, 20, PLAYER_WIDTH, PLAYER_HEIGHT);
    Rectangle computerPaddle = new Rectangle(785, 20, PLAYER_WIDTH, PLAYER_HEIGHT);
    Circle ball = new Circle(500, 500, BALL_RADIUS);

    @Override
    /**
     * Defines the game windows and nodes + some game logic
     */
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
            gameStarted = true;
            gamePlay();
        });

        //GAME SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //create the window widgets of the GAME SCREEN
        Button quit = new Button("QUIT");
        quit.setLayoutX(WIDTH / 2 - 40);
        quit.setLayoutY(HEIGHT / 2);

        // defines the pane hierarchy for the WELCOME SCREEN
        p2.getChildren().addAll(humanPaddle, computerPaddle, ball, quit);

        //EVENT Listeners for GAME SCREEN
        quit.setOnMouseClicked(e -> {
            primaryStage.setScene(overScreen);
        });
        gameScreen.setOnMouseMoved(e -> {
            humanPaddle.setY(e.getY());
        });

        humanPaddle.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case DOWN:
                    humanPaddle.setY(humanPaddle.getY() + 10);
                    break;
                case UP:
                    humanPaddle.setY(humanPaddle.getY() - 10);
                    break;
            }
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
        //shows the window of the application, beginning with the WELCOME SCREEN
        primaryStage.setTitle("P O N G");
        primaryStage.setScene(welcomeScreen);
        primaryStage.show();
    }

    /**
     * Resets all variables and positions to default for game start
     */
    public void resetGame() {
        gameStarted = false;//game not in session

        //Sets ball speed back to zero and position to default
        ballXSpeed = 0;
        ballYSpeed = 0;

        //Set paddle positions and size back to default

        //Sets score back to zero
        playerScore = 0;
    }

    /**
     * Starts the game and defines the game logic.
     */
    public void gamePlay() {
        if (gameStarted) {
            //set ball movement
            ball.setLayoutX((ball.getLayoutX()+ballXSpeed));
            ball.setLayoutY((ball.getLayoutY()+ballYSpeed));

            //simple computer opponent who is following the ball
            if (ballXPos < WIDTH - WIDTH / 4) {
                computerYPos = ballYPos - PLAYER_HEIGHT / 2;
            } else {
                computerYPos = ballYPos > computerYPos + PLAYER_HEIGHT / 2 ? computerYPos += 1 : computerYPos - 1;
            }
        }

    }

}
