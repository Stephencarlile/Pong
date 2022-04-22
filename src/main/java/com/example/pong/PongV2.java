package com.example.pong;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private int lives = 3;
    private int playerXPos = 0;
    private double computerXPos = WIDTH - PLAYER_WIDTH;
    private double playerYPos = WIDTH / 2;
    private double computerYPos = WIDTH / 2;
    private static List<Players> scoreBoard = new ArrayList<Players>();//arrayList to store scores for scoreboards

    //Global node declarations
    //create the window widgets of the GAME SCREEN
    Rectangle humanPaddle = new Rectangle(0, 20, PLAYER_WIDTH, PLAYER_HEIGHT);
    Rectangle computerPaddle = new Rectangle(785, (HEIGHT / 2 - 75), PLAYER_WIDTH, PLAYER_HEIGHT);
    Circle ball = new Circle(0, 0, BALL_RADIUS);

    Label score = new Label("" + playerScore);

    Label currentLives = new Label("" + lives);

    Random rand = new Random();

    //Declares the 3 panes, one for each screen
    Pane p1 = new Pane();
    Pane p2 = new Pane();
    Pane p3 = new Pane();

    //Declare the 3 screens of the game
    Scene welcomeScreen = new Scene(p1, WIDTH, HEIGHT);
    Scene gameScreen = new Scene(p2, WIDTH, HEIGHT);
    Scene overScreen = new Scene(p3, WIDTH, HEIGHT);

    //Ball animation
    PathTransition ptBall = new PathTransition();

    Stage primaryStage = new Stage();

    @Override
    /**
     * Defines the game windows and nodes + some game logic
     */
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
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
        quit.setLayoutX(WIDTH - 50);
        quit.setLayoutY(10);

        Text scoreLabel = new Text("Player Score:");
        scoreLabel.setLayoutX(WIDTH / 2 - 30);
        scoreLabel.setLayoutY(12);

        //Score label
        score.setLayoutX(WIDTH / 2);
        score.setLayoutY(20);

        Text livesLabel = new Text("Lives Left:");
        livesLabel.setLayoutX(WIDTH / 2 - 20);
        livesLabel.setLayoutY(570);

        //Score label
        currentLives.setLayoutX(WIDTH / 2);
        currentLives.setLayoutY(580);


        // defines the pane hierarchy for the WELCOME SCREEN
        p2.getChildren().addAll(humanPaddle, computerPaddle, ball, quit, scoreLabel, score, currentLives, livesLabel);

        //EVENT Listeners for GAME SCREEN
        quit.setOnMouseClicked(e -> {
            primaryStage.setScene(overScreen);
            ptBall.stop();
        });

//        gameScreen.setOnKeyPressed(e -> {
//            switch (e.getCode()) {
//                case DOWN:
//                    humanPaddle.setY(humanPaddle.getY() + 10);
//                    break;
//                case UP:
//                    humanPaddle.setY(humanPaddle.getY() - 10);
//                    break;
//            }
//        });
        gameScreen.setOnMouseMoved(e -> {
            humanPaddle.setY(e.getY());
            //System.out.printf("py: %f, px: %f, by: %f, bx: %f \n", humanPaddle.getY(),humanPaddle.getX(),ball.getTranslateY(),ball.getTranslateX());

        });

        ball.translateYProperty().addListener(ov ->
        {
            computerPaddle.setY((ball.getTranslateY() - PLAYER_HEIGHT / 2));
            if (ball.getTranslateY() >= HEIGHT || ball.getTranslateY() <= 0) {
                ptBall.setRate(ptBall.getRate() * -1);
            }
        });


        //GAME OVER SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //create the window widgets of the GAME OVER SCREEN
        Button quit2 = new Button("QUIT");
        quit2.setLayoutX(WIDTH - 50);
        quit2.setLayoutY(10);
        Button restart = new Button("RESTART");

        Text gameOver = new Text("GAME OVER !");
        gameOver.setX(WIDTH / 2 - 300);
        gameOver.setY(HEIGHT / 2);
        gameOver.setFont((Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 80)));

        Label enterLabel = new Label("Please enter your name to add yourself to the scoreboard!");

        TextField enterName = new TextField();
        // enterName.setDisable(false);

        Button enter = new Button("ENTER");
        //  enter.setDisable(true);

        VBox enterNameArea = new VBox();
        enterNameArea.setLayoutX(WIDTH / 2 - 180);
        enterNameArea.setLayoutY(HEIGHT / 2 + 40);

        //Defines the pane hierarchy for the GAME OVER SCREEN
        enterNameArea.getChildren().addAll(enterLabel, enterName, enter);
        p3.getChildren().addAll(quit2, restart, gameOver, enterNameArea);

        //Event listeners for GAME OVER SCREEN
        enterName.textProperty().addListener(e -> {
            //  enter.setDisable(false);
        });
        enter.setOnMouseClicked(e -> {
            //  enterName.setDisable(true);

            addScores(new Players(enterName.getText(), playerScore));
            System.out.println(scoreBoard);

            TableView table = new TableView();
            table = displayScore();
            enterNameArea.getChildren().add(table);

        });
        quit2.setOnMouseClicked(e -> {
           // enterNameArea.getChildren().remove(table);
            primaryStage.setScene(welcomeScreen);
            resetGame();
        });
        restart.setOnMouseClicked(e -> {
            resetGame();
            primaryStage.setScene(gameScreen);
            ptBall.play();
        });

        // --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
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

        //Sets score back to zero
        playerScore = 0;
        score.setText("" + playerScore);

        //Resets lives
        lives = 3;
        currentLives.setText("" + lives);

        //Reset Rate
        ptBall.setRate(1.0);
    }

    /**
     * Starts the game and defines the game logic.
     */
    public void gamePlay() {
        //BALL ANIMATION
        ptBall.setDuration(javafx.util.Duration.seconds(3));
        Line ballPath = new Line(700, HEIGHT, 15, 100);

        ptBall.setPath(ballPath);//start going left
        ptBall.setNode(ball);
        ptBall.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        ptBall.setInterpolator(Interpolator.LINEAR);
        ptBall.setCycleCount(1);
        ptBall.setAutoReverse(false);

        ptBall.setOnFinished(e -> {
            //if hits the battle, reverse:
            if (ball.getTranslateX() <= 15) {
                if ((humanPaddle.getY() <= ball.getTranslateY() && ball.getTranslateY() <= (humanPaddle.getY() + PLAYER_HEIGHT))) {
                    playerScore++;
                    score.setText("" + playerScore);
                    ptBall.setRate(ptBall.getRate() * -1);
                    if (ptBall.getRate() > 0) {
                        ptBall.setRate(ptBall.getRate() + 0.5);
                    } else {
                        ptBall.setRate(ptBall.getRate() - 0.5);
                    }

                    ptBall.play();

                } else {
                    if (lives > 1) {
                        lives--;
                        currentLives.setText("" + lives);
                        ptBall.setRate(ptBall.getRate() * -1);
                        if (ptBall.getRate() > 0) {
                            ptBall.setRate(ptBall.getRate() + 0.5);
                        } else {
                            ptBall.setRate(ptBall.getRate() - 0.5);
                        }
                        ptBall.play();
                    } else {
                        primaryStage.setScene(overScreen);
                        ptBall.stop();
                    }

                }

            } else {
                if (ball.getTranslateX() >= 785) {
                    if ((computerPaddle.getY() <= ball.getTranslateY() && ball.getTranslateY() <= (computerPaddle.getY() + PLAYER_HEIGHT))) {
                        ptBall.setRate(ptBall.getRate() * -1);
                        ptBall.play();

                    } else {//else, lose a point / Gameover because you missed
                        primaryStage.setScene(overScreen);
                        ptBall.stop();

                    }
                }
            }

        });
        ptBall.play();

    }

    /**
     * Arranges the best scores of all the players and their corresponding names
     */
    public static void addScores(Players p) {
        String name;
        double gpa;
        int index, ind = 0;

        if (scoreBoard.isEmpty()) {
            scoreBoard.add(new Players(p.getName(), p.getScore()));
        } else {
            index = scoreBoard.size();
            if (p.getScore() > scoreBoard.get(index - 1).getScore()) {
                index--;
                while (index > 0) {
                    if (p.getScore() > scoreBoard.get(index - 1).getScore()) {
                        index--;
                    } else {
                        ind = index;
                        index = 0;
                    }
                    ind = index;
                }
                scoreBoard.add(ind, new Players(p.getName(), p.getScore()));
            } else {
                scoreBoard.add(ind, new Players(p.getName(), p.getScore()));
            }
        }
    }

    public TableView displayScore() {
        // New Table View
        TableView tbv = new TableView();

        // Create two columns
        TableColumn<String, Players> cl1 = new TableColumn<>("Player Name");
        cl1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Integer, Players> cl2 = new TableColumn<>("Player Score");
        cl2.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Add two columns into TableView
        tbv.getColumns().add(cl1);
        tbv.getColumns().add(cl2);

        // Load objects into table
        for (Players p : scoreBoard){
            tbv.getItems().add(p);
        }

        return tbv;
    }

}



