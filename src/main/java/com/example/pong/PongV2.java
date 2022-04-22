package com.example.pong;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
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
    //Global variable declarations
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_RADIUS = 15;
    private int playerScore = 0;
    private int lives = 3;
    private static List<Players> scoreBoard = new ArrayList<Players>();//arrayList to store scores for scoreboards

    //Global node declarations
    //create the window widgets of the GAME SCREEN
    Rectangle humanPaddle = new Rectangle(0, 20, PLAYER_WIDTH, PLAYER_HEIGHT);
    Rectangle computerPaddle = new Rectangle(785, (HEIGHT / 2 - 75), PLAYER_WIDTH, PLAYER_HEIGHT);
    Circle ball = new Circle(0, 0, BALL_RADIUS);
    Label score = new Label("" + playerScore);
    Label currentLives = new Label("" + lives);

    //Global window widgets of the GAME OVER SCREEN
    Label finalScore = new Label("" + playerScore);

    //Random object global declaration
    Random rand = new Random();

    // New Table View
    TableView tbv = new TableView();

    //Declares the 3 panes, one for each screen
    Pane p1 = new Pane();
    Pane p2 = new Pane();
    Pane p3 = new Pane();

    //Declare the 3 screens of the game
    Scene welcomeScreen = new Scene(p1, WIDTH, HEIGHT);
    Scene gameScreen = new Scene(p2, WIDTH, HEIGHT);
    Scene overScreen = new Scene(p3, WIDTH, HEIGHT);

    //Ball animation declarations
    PathTransition ptBall = new PathTransition();

    //Stage declaration
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
            finalScore.setText("" + playerScore);
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

        Button restart = new Button("Play Again");
        restart.setLayoutY(10);

        Text gameOver = new Text("GAME OVER !");
        gameOver.setX(WIDTH / 2 - 300);
        gameOver.setY(HEIGHT / 2 - 100);
        gameOver.setFont((Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 80)));

        Label enterLabel = new Label("Please enter your name to add yourself to the scoreboard!");

        Text finalScoreText = new Text("Your score: ");


        HBox finalScoresHBox = new HBox();
        finalScoresHBox.setLayoutX(WIDTH / 2 - 75);
        finalScoresHBox.setLayoutY(HEIGHT / 2 - 50);

        finalScoresHBox.getChildren().addAll(finalScoreText, finalScore);

        TextField enterName = new TextField();

        Button enter = new Button("ENTER");

        VBox enterNameArea = new VBox();
        enterNameArea.setLayoutX(WIDTH / 2 - 180);
        enterNameArea.setLayoutY(HEIGHT / 2);

        // Create two columns
        TableColumn<String, Players> cl1 = new TableColumn<>("Player Name");
        cl1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Integer, Players> cl2 = new TableColumn<>("Player Score");
        cl2.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Add two columns into TableView
        tbv.getColumns().add(cl1);
        tbv.getColumns().add(cl2);

        //Defines the pane hierarchy for the GAME OVER SCREEN
        enterNameArea.getChildren().addAll(enterLabel, enterName, enter, tbv);
        tbv.setVisible(false);
        p3.getChildren().addAll(quit2, restart, gameOver, enterNameArea, finalScoresHBox);

        //Event listeners for GAME OVER SCREEN
        enterName.textProperty().addListener(e -> {
            //  enter.setDisable(false);
        });
        enter.setOnMouseClicked(e -> {
            addScores(new Players(enterName.getText(), playerScore));
            System.out.println(scoreBoard);
            updateBoard();
            tbv.setVisible(true);
            enterName.setText("");
            enter.setDisable(true);

        });
        quit2.setOnMouseClicked(e -> {
            enter.setDisable(false);
            resetGame();
            primaryStage.setScene(welcomeScreen);

        });
        restart.setOnMouseClicked(e -> {
            enter.setDisable(false);
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
        //Sets score back to zero
        playerScore = 0;
        score.setText("" + playerScore);

        //Resets lives
        lives = 3;
        currentLives.setText("" + lives);

        //Resets final score
        finalScore.setText("" + playerScore);

        //Reset Rate
        ptBall.setRate(1.0);

        //Clears scorebaord and hides it
        tbv.setVisible(false);
        tbv.getItems().clear();


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
                        finalScore.setText("" + playerScore);
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
                        finalScore.setText("" + playerScore);
                        ptBall.stop();

                    }
                }
            }

        });
        ptBall.play();

    }

    /**
     * Arranges the best scores of all the players and their corresponding names ordered from highest to lowest scores
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

    /**
     * Updates the scoreboard
     */
    public void updateBoard() {
        //clears the table so that names are not doubled
        tbv.getItems().clear();

        // Load objects into table
        for (Players p : scoreBoard) {
            tbv.getItems().add(p);
        }
    }

}



