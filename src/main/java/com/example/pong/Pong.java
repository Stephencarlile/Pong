package com.example.pong;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
 * Makes use of Players.java class for scoreboard feature
 */
public class Pong extends Application {
    //Global variable declarations
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_HEIGHT = 100;
    private static final int PLAYER_WIDTH = 15;
    private static final double BALL_RADIUS = 15;
    private int playerScore = 0;
    private int lives = 3;
    private static List<Players> scoreBoard = new ArrayList<Players>();//arrayList to store scores for scoreboards
    private boolean highContrast = false;
    private Color backgroundColor = Color.WHITE;
    private Color fontColor = Color.BLACK;
    private Color paddleColor = Color.BLACK;
    private Color ballColor = Color.BLACK;
    //    private double currentSpeed = 1;
    private boolean gameStarted = false;

    //Random object global declaration
    Random rand = new Random();
    //Lines for path animation
    private Line ballPath = new Line(15, 100, 700, HEIGHT);
    // private Line ballStartPath = new Line(785, 20, 100, HEIGHT);
    //private Line ballStartPath = new Line(15, rand.nextInt(HEIGHT), 700, HEIGHT);
    //private Line ballStartPath = new Line(15, 20, 700, HEIGHT);
    //private Line ballStartPath = new Line(15, 600, 500, 0);//hits top right  from bottom left
    //private Line ballStartPath = new Line(785, 600, 200, 0);//hits top left  from bottom right
    //private Line ballStartPath = new Line(785, 20, 200,HEIGHT);//hits bottom left from top right
    // private Line ballStartPath = new Line(15, 20, 600, HEIGHT);//hits bottom right from top left
    //private Line ballStartPath = new Line(WIDTH-PLAYER_WIDTH, HEIGHT/2, 15, HEIGHT/2);//hits bottom right from top left
private Line ballStartPath = new Line(WIDTH-PLAYER_WIDTH, rand.nextInt(HEIGHT), 15, rand.nextInt(HEIGHT));

    //Global node declarations
    //global window widgets for the START SCREEN
    CheckBox checkForContrast = new CheckBox("Check for High Contrast");

    //create the window widgets of the GAME SCREEN
    Rectangle humanPaddle = new Rectangle(0, 20, PLAYER_WIDTH, PLAYER_HEIGHT);
    Rectangle computerPaddle = new Rectangle(785, (HEIGHT / 2 - 75), PLAYER_WIDTH, PLAYER_HEIGHT);
    Circle ball = new Circle(0, 0, BALL_RADIUS);

    Label score = new Label("" + playerScore);
    Label currentLives = new Label("" + lives);

    //Ball animation declarations
    PathTransition ptBall = new PathTransition();

    //Global window widgets of the GAME OVER SCREEN
    Label finalScore = new Label("" + playerScore);
    // New Table View
    TableView tbv = new TableView();

    //-----------------------------------------
    //Declares the 3 panes, one for each screen
    Pane p1 = new Pane();
    Pane p2 = new Pane();
    Pane p3 = new Pane();

    //Declare the 3 screens of the game
    Scene welcomeScreen = new Scene(p1, WIDTH, HEIGHT);
    Scene gameScreen = new Scene(p2, WIDTH, HEIGHT);
    Scene overScreen = new Scene(p3, WIDTH, HEIGHT);

    //Stage declaration
    Stage primaryStage = new Stage();

    //----------------------------------------------------
    @Override
    /**
     * Defines the game windows and nodes + some game logic
     */
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        //WELCOME SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //create the window widgets of the WELCOME SCREEN
        welcomeScreen.setFill(backgroundColor);
        Text welcome = new Text(WIDTH / 2 - 100, HEIGHT / 3, "Welcome to PONG!");
        welcome.setFont(Font.font("Verdana", 20));
        welcome.setFill(fontColor);

        Button start = new Button("Start Game");
        start.setLayoutX(WIDTH / 2 - 40);
        start.setLayoutY(HEIGHT / 2);
        start.setScaleX(1.5);
        start.setScaleY(1.5);
        start.setTextFill(fontColor);

        Text instructions = new Text(WIDTH / 2 - 40, HEIGHT - 100, "Instructions");
        instructions.setFill(fontColor);

        //Check for contrast button
        checkForContrast.setLayoutY(HEIGHT - 20);
        checkForContrast.setTextFill(fontColor);

        // define the pane hierarchy for the WELCOME SCREEN
        p1.getChildren().addAll(welcome, start, instructions, checkForContrast);

        //EVENT Listeners for WELCOME SCREEN
        start.setOnMouseClicked(e -> {
            primaryStage.setScene(gameScreen);
            startGame();

        });
        if (highContrast) {
            backgroundColor = Color.BLACK;
            fontColor = Color.WHITE;
            paddleColor = Color.WHITE;
            ballColor = Color.WHITE;

        }
        checkForContrast.selectedProperty().addListener(e -> {
            highContrast = true;
            welcomeScreen.setFill(backgroundColor);
            instructions.setFill(fontColor);
            start.setTextFill(fontColor);
            welcome.setFill(fontColor);


        });

        //GAME SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        gameScreen.setFill(backgroundColor);
        humanPaddle.setFill(paddleColor);
        computerPaddle.setFill(paddleColor);
        ball.setFill(ballColor);
        //create the window widgets of the GAME SCREEN
        Button quit = new Button("QUIT");
        quit.setLayoutX(WIDTH - 50);
        quit.setLayoutY(10);
        quit.setTextFill(fontColor);

        Text scoreLabel = new Text("Player Score:");
        scoreLabel.setLayoutX(WIDTH / 2 - 30);
        scoreLabel.setLayoutY(12);
        scoreLabel.setFill(fontColor);

        //Score label
        score.setLayoutX(WIDTH / 2);
        score.setLayoutY(20);
        scoreLabel.setFill(fontColor);

        Text livesLabel = new Text("Lives Left:");
        livesLabel.setLayoutX(WIDTH / 2 - 20);
        livesLabel.setLayoutY(570);
        livesLabel.setFill(fontColor);

        //Lives label
        currentLives.setLayoutX(WIDTH / 2);
        currentLives.setLayoutY(580);
        currentLives.setTextFill(fontColor);

        // defines the pane hierarchy for the WELCOME SCREEN
        p2.getChildren().addAll(humanPaddle, computerPaddle, ball, quit, scoreLabel, score, currentLives, livesLabel);

        //EVENT Listeners for GAME SCREEN
        quit.setOnMouseClicked(e -> {
            finalScore.setText("" + playerScore);
            primaryStage.setScene(overScreen);
            ptBall.stop();
        });

        ball.translateYProperty().addListener(ov ->
        {
            //computer paddle follows the ball y value
            computerPaddle.setY((ball.getTranslateY() - PLAYER_HEIGHT / 2));


        });
        //Follows the X property of ball and checks if it hits paddles or not, updates scores and lives accordingly
        ball.translateXProperty().addListener(e -> {
            animateX();
        });
        ball.translateYProperty().addListener(e -> {
            animateY();

        });

        ptBall.pathProperty().addListener(e -> {
            ballPath = (Line) ptBall.getPath();
            System.out.println(ballPath);

        });

        gameScreen.setOnMouseMoved(e -> {
            humanPaddle.setY(e.getY());
            //debugging purposes
            //System.out.printf("py: %f, px: %f, by: %f, bx: %f \n", humanPaddle.getY(),humanPaddle.getX(),ball.getTranslateY(),ball.getTranslateX());
        });

//        gameScreen.setOnKeyPressed(e -> {
//            switch (e.getCode()) {
//                case DOWN:
//                    humanPaddle.setY(humanPaddle.getY() - 10);
//                    break;
//                case UP:
//                    humanPaddle.setY(humanPaddle.getY() + 10);
//                    break;
//            }
//        });

        //GAME OVER SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        overScreen.setFill(backgroundColor);
        //create the window widgets of the GAME OVER SCREEN
        Button quit2 = new Button("QUIT");
        quit2.setLayoutX(WIDTH - 50);
        quit2.setLayoutY(10);
        quit2.setTextFill(fontColor);

        Button restart = new Button("Play Again");
        restart.setLayoutY(10);
        restart.setTextFill(fontColor);

        Text gameOver = new Text("GAME OVER !");
        gameOver.setX(WIDTH / 2 - 300);
        gameOver.setY(HEIGHT / 2 - 100);
        gameOver.setFont((Font.font("Verdana", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 80)));
        gameOver.setFill(fontColor);

        Label enterLabel = new Label("Please enter your name to add yourself to the scoreboard!");
        enterLabel.setTextFill(fontColor);
        Text finalScoreText = new Text("Your score: ");
        finalScoreText.setFill(fontColor);

        HBox finalScoresHBox = new HBox();
        finalScoresHBox.setLayoutX(WIDTH / 2 - 75);
        finalScoresHBox.setLayoutY(HEIGHT / 2 - 50);

        finalScoresHBox.getChildren().

                addAll(finalScoreText, finalScore);

        TextField enterName = new TextField();
        Button enter = new Button("ENTER");
        enter.setTextFill(fontColor);

        VBox enterNameArea = new VBox();
        enterNameArea.setLayoutX(WIDTH / 2 - 180);
        enterNameArea.setLayoutY(HEIGHT / 2);

        // Create two columns for the scoreboard table
        TableColumn<String, Players> cl1 = new TableColumn<>("Player Name");
        cl1.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Integer, Players> cl2 = new TableColumn<>("Player Score");
        cl2.setCellValueFactory(new PropertyValueFactory<>("score"));

        // Add two columns into TableView for the scoreboard table
        tbv.getColumns().

                add(cl1);
        tbv.getColumns().

                add(cl2);

        //Defines the pane hierarchy for the GAME OVER SCREEN
        enterNameArea.getChildren().

                addAll(enterLabel, enterName, enter, tbv);
        tbv.setVisible(false);
        p3.getChildren().

                addAll(quit2, restart, gameOver, enterNameArea, finalScoresHBox);

        //Event listeners for GAME OVER SCREEN
        enterName.textProperty().

                addListener(e ->

                {
                    //  enter.setDisable(false);
                });
        enter.setOnMouseClicked(e ->

        {
            addScores(new Players(enterName.getText(), playerScore));
            System.out.println(scoreBoard);
            updateBoard();
            tbv.setVisible(true);
            enterName.setText("");
            enter.setDisable(true);

        });
        quit2.setOnMouseClicked(e ->

        {
            enter.setDisable(false);
            resetGame();
            primaryStage.setScene(welcomeScreen);

        });
        restart.setOnMouseClicked(e ->

        {
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

        //Resets lives to 3
        lives = 3;
        currentLives.setText("" + lives);

        //Resets final score
        finalScore.setText("" + playerScore);

        //Reset Animation Rate to default
        ptBall.setRate(1.0);
        ptBall.setPath(ballStartPath);

        //Clears scorebaord and hides it
        tbv.setVisible(false);
        tbv.getItems().clear();

        //Unselects the checkbox ?
        checkForContrast.setSelected(false);

        //Reset animation path
        // ballStartPath = new Line(15, rand.nextInt(HEIGHT), 700, HEIGHT);
        ballStartPath = new Line(15, 20, 600, HEIGHT);
        ptBall.stop();
        ptBall.setPath(ballStartPath);

    }

    /**
     * Starts the game by initializing the game logic and begins animation
     */
    public void startGame() {
        //BALL ANIMATION
        ptBall.setDuration(javafx.util.Duration.seconds(3));
        // Line ballPath = new Line(700, HEIGHT, 15, 100);
        ptBall.setPath(ballStartPath);//start going left
        ptBall.setNode(ball);
        ptBall.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        ptBall.setInterpolator(Interpolator.LINEAR);
        ptBall.setCycleCount(1);
        ptBall.setAutoReverse(false);

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
     * Updates the scoreboard by clearing it and resetting with the ArrayList of names
     */
    public void updateBoard() {
        //clears the table so that names are not doubled
        tbv.getItems().clear();

        // Load objects into table
        for (Players p : scoreBoard) {
            tbv.getItems().add(p);
        }
    }

    /**
     * Returns an ArrayList holding the slope and y-intercept of a line given two points.
     */
    public ArrayList<Double> calcSlopeAndInt(double x1, double y1, double x2, double y2) {
        double m = 0;
        double b = 0;

        m = (y2 - y1) / (x2 - x1);
        b = y1 - m * x1;

        ArrayList<Double> mAndB = new ArrayList<Double>();

        mAndB.add(m);
        mAndB.add(b);

        return mAndB;

        //b = m(-15) + yA---> y=mx+b ==> b=y-mx
        // System.out.println("Start Y: " + ballPath.getStartY());
        //System.out.println("the y intercept: " + slope * (-15) + ballPath.getStartY());
    }


//    /**
//     * Tied to the event listener for the ball animation, this method controls when and how the ball will bounce back and forth when it hits each paddle, or not
//     * The majority of the game/animation logic is here
//     */
//    public void animate() {
//        if (ball.getTranslateX() <= PLAYER_WIDTH) {
//            ptBall.stop();
//            //if near the human paddle
//            if (humanPaddle.getY() <= ball.getTranslateY() && ball.getTranslateY() <= (humanPaddle.getY() + PLAYER_HEIGHT)) {
//                //if it touches the human paddle
//                System.out.println("Hit the human paddle");
//                playerScore++;
//                score.setText("" + playerScore);
//                //ptBall.stop();
//                ptBall.setPath(hitSideAndBounce());
//                ptBall.play();
//
//            } else {
//                //if it misses the paddle
//                System.out.println("Missed the human paddle");
//                if (lives > 1) {
//                    //if there are still lives left
//                    System.out.println("Lives left");
//                    lives--;
//                    currentLives.setText("" + lives);
//                    // ptBall.stop();
//                    ptBall.setPath(hitSideAndBounce());
//                    ptBall.play();
//                } else {
//                    //missed paddle and no more lives--> end game
//                    System.out.println("No more lives");
//                    primaryStage.setScene(overScreen);
//                    finalScore.setText("" + playerScore);
//                    ptBall.stop();
//                }
//
//            }
//
//        } else if (ball.getTranslateX() >= 785 && (computerPaddle.getY() <= ball.getTranslateY() && (ball.getTranslateY() <= (computerPaddle.getY() + PLAYER_HEIGHT)))) {
//            //if near the computer paddle
//            ptBall.stop();
//            System.out.println("Hit the computer paddle");
//            //ptBall.stop();
//            ptBall.setPath(hitSideAndBounce());
//            ptBall.play();
//
//        }
//    }

    public Line clone(Line l) {
        return new Line(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY());
    }

    /**
     * Keeps the ball animating within the y values of the screen and makes the ball bounce as it should if it hits the top or bottom
     */
    public void animateY() {
        //System.out.println("animateY called");
        if (ball.getTranslateY() == HEIGHT){//  && (ball.getTranslateX()>PLAYER_WIDTH&&ball.getTranslateX()<WIDTH-PLAYER_WIDTH)) {
            //Ball hitting the bottom of the screen
            System.out.println("hitting the bottom coming from the left");
            double touchX = ballPath.getEndX();
            double touchY = HEIGHT;
            double d = touchX - ballPath.getStartX();
            double nY = ballPath.getStartY();
            double nX;

            if (ballPath.getStartX() <= WIDTH / 2) {

                //ball coming from the left
                 nX = touchX + d;

            } else {
                //ball coming from the right
                nX = touchX - d;
            }

            double oldDistance = Math.sqrt((Math.pow(ballPath.getStartX() - touchX, 2) + Math.pow(ballPath.getStartY() - touchY, 2)));
            double nDistance = Math.sqrt((Math.pow(nX - touchX, 2) + Math.pow(nY - touchY, 2)));
            double oldRate = ptBall.getRate();
            double nRate = (oldRate * nDistance) / oldDistance;

            ArrayList<Double> lineCoefficients = calcSlopeAndInt(touchX, touchY, nX, nY);

            if (ballPath.getStartX() <= WIDTH / 2) {

                //ball coming from the left
                nX = WIDTH - PLAYER_WIDTH;


            } else {
                //ball coming from the right
                nX = PLAYER_WIDTH;
            }
            nY=ballPath.getStartY();
            //nY = lineCoefficients.get(0) * nX + lineCoefficients.get(1);

            if (nY < 0) {
                System.out.println("adjusting y to be pos");
                nY = 0 - nY;
            } else if (nY > HEIGHT) {
                System.out.println("adjusting y to be less than height");
                nY = nY - HEIGHT;
            }

            ptBall.stop();
            ptBall.setPath(new Line(touchX, touchY, nX, nY));
            ptBall.setRate(nRate);
            ptBall.play();
        }
        if (ball.getTranslateY() == 0){// &&(ball.getTranslateX()>PLAYER_WIDTH&&ball.getTranslateX()<WIDTH-PLAYER_WIDTH)) {
            //ball hits the top of the window
            double touchX = ballPath.getEndX();
            double touchY = 0;
            double d = touchX - ballPath.getStartX();
            double nY = ballPath.getStartY();
            double nX;

            if (ballPath.getStartX() <= WIDTH / 2) {

                //ball coming from the left
                nX = touchX + d;

            } else {
                //ball coming from the right
                nX = touchX - d;
            }

            double oldDistance = Math.sqrt((Math.pow(ballPath.getStartX() - touchX, 2) + Math.pow(ballPath.getStartY() - touchY, 2)));
            double nDistance = Math.sqrt((Math.pow(nX - touchX, 2) + Math.pow(nY - touchY, 2)));
            double oldRate = ptBall.getRate();
            double nRate = (oldRate * nDistance) / oldDistance;

            ArrayList<Double> lineCoefficients = calcSlopeAndInt(touchX, touchY, nX, nY);

            if (ballPath.getStartX() <= WIDTH / 2) {
                //ball coming from the left
                nX = WIDTH - PLAYER_WIDTH;

            } else {
                //ball coming from the right
                nX = PLAYER_WIDTH;
            }

           // nY = lineCoefficients.get(0) * nX + lineCoefficients.get(1);
            nY=ballPath.getStartY();
            if (nY < 0) {
                System.out.println("adjusting y");
                nY = 0 - nY;
            } else if (nY > HEIGHT) {
                nY = nY - HEIGHT;
            }

            ptBall.stop();
            ptBall.setPath(new Line(touchX, touchY, nX, nY));
            ptBall.setRate(nRate);
            ptBall.play();
        }
    }

    /**
     * Keeps the ball animating on the screen and checks if it hits the human paddle and updates scores/lives accordingly
     */
    public void animateX() {
        //System.out.println("animateX called");
        if (ball.getTranslateX() == WIDTH - PLAYER_WIDTH) {

            double touchX = WIDTH - PLAYER_WIDTH;
            double touchY = ballPath.getEndY();
            double d = ballPath.getStartY() - touchY;
            double nY = touchY - d;
            double nX = ballPath.getStartX();

            double oldDistance = Math.sqrt((Math.pow(ballPath.getStartX() - touchX, 2) + Math.pow(ballPath.getStartY() - touchY, 2)));
            double nDistance = Math.sqrt((Math.pow(nX - touchX, 2) + Math.pow(nY - touchY, 2)));
            double oldRate = ptBall.getRate();
            double nRate = (oldRate * nDistance) / oldDistance;

            ArrayList<Double> lineCoefficients = calcSlopeAndInt(touchX, touchY, nX, nY);

            if (ballPath.getStartY() >= HEIGHT / 2) {
                //hit the computer paddle from the bottom half of the screen
                nY = 0;
            } else {
                //hit the computer paddle from the top half of the screen
                nY = HEIGHT;
            }
            nX = ((1 / lineCoefficients.get(0)) * (nY - lineCoefficients.get(1)));

            if (nX < PLAYER_WIDTH) {
                System.out.println("adjusting x to be equal to player width");
                nX = PLAYER_HEIGHT;
            } else if (nX > WIDTH - PLAYER_WIDTH) {
                System.out.println("adjusting x to be equal to the width");
                nX = WIDTH - PLAYER_WIDTH;
            }

            ptBall.stop();
            ptBall.setPath(new Line(touchX, touchY, nX, nY));
            ptBall.setRate(nRate);
            ptBall.play();


        }
        if (ball.getTranslateX() == PLAYER_WIDTH) {

            double touchX = PLAYER_WIDTH;
            double touchY = ballPath.getEndY();
            double d = ballPath.getStartY() - touchY;
            double nY = touchY - d;
            double nX = ballPath.getStartX();

            double oldDistance = Math.sqrt((Math.pow(ballPath.getStartX() - touchX, 2) + Math.pow(ballPath.getStartY() - touchY, 2)));
            double nDistance = Math.sqrt((Math.pow(nX - touchX, 2) + Math.pow(nY - touchY, 2)));
            double oldRate = ptBall.getRate();
            double nRate = (oldRate * nDistance) / oldDistance;

            ArrayList<Double> lineCoefficients = calcSlopeAndInt(touchX, touchY, nX, nY);

            if (ballPath.getStartY() >= HEIGHT / 2) {
                //hit the computer paddle from the bottom half of the screen
                nY = 0;
            } else {
                //hit the computer paddle from the top half of the screen
                nY = HEIGHT;
            }
            nX = (1 / lineCoefficients.get(0) * (nY - lineCoefficients.get(1)));
            if (nX < PLAYER_WIDTH) {
                System.out.println("adjusting x to be equal to player width");
                nX = PLAYER_HEIGHT;
            } else if (nX > WIDTH - PLAYER_WIDTH) {
                System.out.println("adjusting x to be equal to the width");
                nX = WIDTH - PLAYER_WIDTH;
            }

            if (humanPaddle.getY() <= ball.getTranslateY() && ball.getTranslateY() <= (humanPaddle.getY() + PLAYER_HEIGHT)) {
                //if it touches the human paddle
                playerScore++;
                score.setText("" + playerScore);
                ptBall.stop();
                ptBall.setPath(new Line(touchX, touchY, nX, nY));
                ptBall.setRate(nRate);
                ptBall.play();

            } else {
                //if it misses the paddle
                System.out.println("Missed the human paddle");
                if (lives > 1) {
                    //if there are still lives left
                    System.out.println("Lives left");
                    lives--;
                    currentLives.setText("" + lives);
                    ptBall.stop();
                    ptBall.setPath(new Line(touchX, touchY, nX, nY));
                    ptBall.setRate(nRate);
                    ptBall.play();
                } else {
                    //missed paddle and no more lives--> end game
                    System.out.println("No more lives");
                    primaryStage.setScene(overScreen);
                    finalScore.setText("" + playerScore);
                    ptBall.stop();
                }
            }

        }
    }
}





