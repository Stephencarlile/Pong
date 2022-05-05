package com.example.pong;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;


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
    public boolean checkingWord = false;
    final String[] typed = {""};
    double speedRate = 1;

    //Normal Colors
    private Color backgroundColor = Color.BLACK;
    private Color fontColor = Color.BLACK;
    private Color welcomeFont = Color.CYAN;
    private Color instructionsColor = Color.LIME;
    private Color ballColor = Color.LIME;
    private Color checkConstrastColor = Color.FUCHSIA;

    //High Contrast Colors
    private Color fontHighContrast = Color.WHITE;
    private Color paddleAndBall = Color.WHITE;

    //Random object global declaration
    Random rand = new Random();

    //Lines for path animation
    private Line ballPath = new Line(15, 100, 700, HEIGHT);
    private Line ballStartPath = new Line(WIDTH - PLAYER_WIDTH, rand.nextInt(HEIGHT - 10) + 10, 15, rand.nextInt(HEIGHT - 10) + 10);

    //GLOBAL NODE DECLARATIONS
    //global window widgets for the START SCREEN
    CheckBox checkForContrast = new CheckBox("Check for High Contrast");

    //create the window widgets of the GAME SCREEN
    Rectangle humanPaddle = new Rectangle(0, 20, PLAYER_WIDTH, PLAYER_HEIGHT);
    Rectangle computerPaddle = new Rectangle(785, (HEIGHT / 2 - 75), PLAYER_WIDTH, PLAYER_HEIGHT);
    Circle ball = new Circle(0, 0, BALL_RADIUS);
    Label score = new Label("" + playerScore);
    Label currentLives = new Label("" + lives);
    Text type2AddLives = new Text("Type the following word in the next second to increase your number of lives! \n The word is: ");

    //Ball global animation declarations
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

    //Declares the 3 screens/scenes of the game
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
        //Sets pane background color
        p1.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        Text welcome = new Text(160, HEIGHT / 3, "Welcome to PONG!");
        welcome.setFont(Font.font("Silom", FontWeight.EXTRA_BOLD, 50));
        welcome.setFill(welcomeFont);

        Button start = new Button("Start Game");
        start.setLayoutX(340);
        start.setLayoutY(HEIGHT / 2);
        start.setScaleX(1.5);
        start.setScaleY(1.5);
        start.setTextFill(fontColor);
        start.setFont(Font.font("Silom"));
        start.setTextFill(Color.WHITE);
        start.setStyle("-fx-background-color: #ff0000; ");

        Text instructions = new Text(30, HEIGHT - 150, "Instructions: \nThe goal of the game is to hit the " +
                "ball as many times as possible with your paddle. \nEach time you hit the ball, you receive 1 point." +
                "You have 3 lives, each time you miss the ball, you lose a life. \nAfter 3 lives are lost, the game is over." +
                "\nAt the end of the game, enter your name to record your score for the player scoreboard.");
        instructions.setFill(instructionsColor);
        instructions.setTextAlignment(TextAlignment.CENTER);
        instructions.setFont(Font.font("Silom", 13));

        //Checkbox for contrast
        checkForContrast.setLayoutY(HEIGHT - 20);
        checkForContrast.setTextFill(checkConstrastColor);
        checkForContrast.setFont(Font.font("Silom"));

        // define the pane hierarchy for the WELCOME SCREEN
        p1.getChildren().addAll(welcome, start, instructions, checkForContrast);

        //EVENT Listeners for WELCOME SCREEN
        start.setOnMouseClicked(e -> {
            primaryStage.setScene(gameScreen);
            startGame();

        });
        //GAME SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //Sets pane colors + its nodes
        p2.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        humanPaddle.setFill(Color.BLUE);
        computerPaddle.setFill(Color.RED);
        ball.setFill(ballColor);

        //create the window widgets of the GAME SCREEN
        Button quit = new Button("QUIT");
        quit.setLayoutX(WIDTH - 60);
        quit.setLayoutY(10);
        quit.setTextFill(fontColor);
        quit.setFont(Font.font("Silom", 16));
        quit.setStyle("-fx-background-color: #FF00FF; ");

        Text scoreLabel = new Text("Player Score:");
        scoreLabel.setLayoutX(WIDTH / 2 - 50);
        scoreLabel.setLayoutY(16);
        scoreLabel.setFont(Font.font("Silom", 16));

        //Score label
        score.setLayoutX(WIDTH / 2);
        score.setLayoutY(20);
        scoreLabel.setFill(Color.CYAN);
        score.setFont(Font.font("Silom", 16));
        score.setTextFill(Color.CYAN);

        //lives label
        Text livesLabel = new Text("Lives Left:");
        livesLabel.setLayoutX(WIDTH / 2 - 45);
        livesLabel.setLayoutY(570);
        livesLabel.setFill(Color.CYAN);
        livesLabel.setFont(Font.font("Silom", 16));

        currentLives.setLayoutX(WIDTH / 2);
        currentLives.setLayoutY(580);
        currentLives.setTextFill(Color.CYAN);
        currentLives.setFont(Font.font("Silom", 14));

        type2AddLives.setX(250);
        type2AddLives.setY(300);

        // defines the pane hierarchy for the WELCOME SCREEN
        p2.getChildren().addAll(humanPaddle, computerPaddle, ball, quit, scoreLabel, score, currentLives, livesLabel, type2AddLives);
        type2AddLives.setVisible(false);

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
        //Follows the Y property of ball and checks if it hits paddles or not, updates scores and lives accordingly
        ball.translateYProperty().addListener(e -> {
            animateY();

        });

        ptBall.pathProperty().addListener(e -> {
            ballPath = (Line) ptBall.getPath();
            System.out.println(ballPath);

        });

        gameScreen.setOnMouseMoved(e -> {
            humanPaddle.setY(e.getY());
        });

        gameScreen.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            // System.out.println(e);
            switch (e.getCode()) {
                case DOWN:
                    humanPaddle.setY(humanPaddle.getY() + 30);
                    break;
                case UP:
                    humanPaddle.setY(humanPaddle.getY() - 30);
                    break;
            }
        });

        gameScreen.setOnKeyPressed(e -> {
            if (checkingWord) {
                System.out.println(e.getCode().getChar());
                typed[0] = typed[0] + e.getCode().getChar();
            }
        });


        //GAME OVER SCREEN----------------------------------------------------------------------------------------------------------------------------------------------------------------------
        //sets pane color for game over screen
        p3.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        //create the window widgets of the GAME OVER SCREEN
        Button quit2 = new Button("QUIT");
        quit2.setLayoutX(WIDTH - 50);
        quit2.setLayoutY(10);
        quit2.setTextFill(fontColor);
        quit2.setFont(Font.font("Silom"));
        quit2.setStyle("-fx-background-color: #FF00FF; ");

        Button restart = new Button("Play Again");
        restart.setLayoutY(10);
        restart.setTextFill(fontColor);
        restart.setFont(Font.font("Silom"));
        restart.setStyle("-fx-background-color: #FF00FF; ");

        //Creates Game Over Image(s)
        Image gameOverImage = new Image("file:gameoverimage.jpg");
        Image gameOverImageBW = new Image("file:gameoverBW.jpeg");

        ImageView viewGameOverImage = new ImageView(gameOverImage);
        viewGameOverImage.setY(25);
        viewGameOverImage.setX(200);
        viewGameOverImage.setFitWidth(400);
        viewGameOverImage.setPreserveRatio(true);
        viewGameOverImage.setSmooth(true);
        viewGameOverImage.setCache(true);

        Label enterLabel = new Label("Please enter your name to add yourself to the scoreboard!");
        enterLabel.setTextFill(Color.LIMEGREEN);
        enterLabel.setFont(Font.font("Silom", 16));
        enterLabel.setTranslateX(-60);
        Text finalScoreText = new Text("Your score: ");
        finalScoreText.setFill(Color.LIMEGREEN);
        finalScoreText.setFont(Font.font("Silom", 20));
        finalScore.setTextFill(Color.LIMEGREEN);
        finalScore.setFont(Font.font("Silom", 20));
        finalScoreText.setTranslateX(-50);
        finalScore.setTranslateX(-30);
        finalScore.setTranslateY(-2);

        HBox finalScoresHBox = new HBox();
        finalScoresHBox.setLayoutX(WIDTH / 2 - 50);
        finalScoresHBox.setLayoutY(HEIGHT / 2 - 50);

        finalScoresHBox.getChildren().

                addAll(finalScoreText, finalScore);

        TextField enterName = new TextField();
        enterName.setTranslateX(-60);
        Button enter = new Button("ENTER");
        enter.setTextFill(fontColor);
        enter.setStyle("-fx-background-color: #FF00FF; ");
        enter.setFont(Font.font("Silom"));
        enter.setTranslateX(150);
        enter.setTranslateY(10);

        //VBOX holds the text field and enter button for scoreboard function
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

        tbv.setTranslateX(-60);
        tbv.setTranslateY(20);

        //Defines the pane hierarchy for the GAME OVER SCREEN
        enterNameArea.getChildren().addAll(enterLabel, enterName, enter, tbv);
        tbv.setVisible(false);
        p3.getChildren().addAll(quit2, restart, enterNameArea, finalScoresHBox, viewGameOverImage);

        //Event listeners for GAME OVER SCREEN

        enter.setOnMouseClicked(e ->

        {
            try {
                addScores(new Players(enterName.getText(), playerScore));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(scoreBoard);
            try {
                updateBoard();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
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

        //Listens to the checkbox for contrast and updates accordingly
        checkForContrast.setOnAction((event) -> {
            highContrast = checkForContrast.isSelected();
            System.out.println(highContrast);
            if (highContrast == true) {//high contrast is selected
                //Set colors on Welcome Screen
                welcome.setFill(fontHighContrast);
                instructions.setFill(fontHighContrast);
                start.setStyle("");
                start.setStyle("{ -fx-text-fill: white; }");
                start.setTextFill(Color.BLACK);
                checkForContrast.setTextFill(fontHighContrast);

                //Set colors on Game Screen
                quit.setStyle("{ -fx-text-fill: white; }");
                ball.setFill(paddleAndBall);
                humanPaddle.setFill(paddleAndBall);
                computerPaddle.setFill(paddleAndBall);
                scoreLabel.setFill(fontHighContrast);
                score.setTextFill(fontHighContrast);
                livesLabel.setFill(fontHighContrast);
                currentLives.setTextFill(fontHighContrast);
                type2AddLives.setFill(Color.CYAN);


                //Set Colors on Game Over Screen
                quit2.setStyle("{ -fx-text-fill: white; }");
                restart.setStyle("{ -fx-text-fill: white; }");
                enterLabel.setTextFill(fontHighContrast);
                finalScoreText.setFill(fontHighContrast);
                finalScore.setTextFill(fontHighContrast);
                enter.setStyle("{ -fx-text-fill: white; }");
                viewGameOverImage.setImage(gameOverImageBW);


            } else {//hi contrast is not selected
                //Set colors on Welcome Screen
                welcome.setFill(welcomeFont);
                instructions.setFill(instructionsColor);
                start.setStyle("-fx-background-color: #ff0000; ");
                checkForContrast.setTextFill(checkConstrastColor);

                // Set colors on Game Screen
                quit.setStyle("-fx-background-color: #FF00FF; ");
                ball.setFill(ballColor);
                humanPaddle.setFill(Color.BLUE);
                computerPaddle.setFill(Color.RED);
                scoreLabel.setFill(welcomeFont);
                score.setTextFill(welcomeFont);
                livesLabel.setFill(welcomeFont);
                currentLives.setTextFill(welcomeFont);
                type2AddLives.setFill(Color.WHITE);

                //Sets colors Game Over Screen
                quit2.setStyle("-fx-background-color: #FF00FF; ");
                restart.setStyle("-fx-background-color: #FF00FF; ");
                enterLabel.setTextFill(Color.LIMEGREEN);
                finalScoreText.setFill(Color.LIMEGREEN);
                finalScore.setTextFill(Color.LIMEGREEN);
                enter.setStyle("-fx-background-color: #FF00FF; ");
                viewGameOverImage.setImage(gameOverImage);


            }

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

        //Clears scoreboard and hides it
        tbv.setVisible(false);
        tbv.getItems().clear();

        //Maintains the selection of the checkbox
        if (highContrast) {
            checkForContrast.setSelected(true);
        } else {
            checkForContrast.setSelected(false);
        }

        //Resets animation path
        ballStartPath = new Line(WIDTH - PLAYER_WIDTH, rand.nextInt(WIDTH - 10) + 10, 15, rand.nextInt(HEIGHT - 10) + 10);
        ptBall.stop();
        ptBall.setPath(ballStartPath);

        ptBall.setDuration(Duration.seconds(3));


    }

    /**
     * Starts the game by initializing the game logic and begins animation
     */
    public void startGame() {
        //BALL ANIMATION initialization
        ptBall.setDuration(javafx.util.Duration.seconds(3));
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
    public static void addScores(Players p) throws IOException, ClassNotFoundException {
        int index, ind = 0;

        ArrayList<Players> list = readScores();

        if (list.isEmpty()) {//scoreboard is empty
            list.add(new Players(p.getName(), p.getScore()));
        } else {
            //the scoreboard is not empty
            index = list.size();
            if (p.getScore() > list.get(index - 1).getScore()) {
                index--;
                while (index > 0) {
                    if (p.getScore() > list.get(index - 1).getScore()) {
                        index--;
                    } else {
                        ind = index;
                        index = 0;
                    }
                }
                list.add(ind, new Players(p.getName(), p.getScore()));
            } else {
                list.add(ind, new Players(p.getName(), p.getScore()));
            }
        }
        saveScoresToFile("players.txt", list);
    }

    /**
     * Updates the scoreboard by clearing it and resetting with the ArrayList of names and scores from a file
     */
    public void updateBoard() throws IOException, ClassNotFoundException {
        //clears the table so that names are not doubled
        tbv.getItems().clear();

        ArrayList<Players> board = readScores();
        Collections.sort(board);

        // Load player objects into table
        for (Players p : board) {
            tbv.getItems().add(p);
        }
    }

    /**
     * Calculate Euclidean distance between 2 points in 2D.
     * p1 = (x1,y1)
     * p2 = (x2,y2)
     *
     * @return distance
     */
    private double calcDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Keeps the ball animating within the y values of the screen and makes the ball bounce as it should if it hits the top or bottom
     * Game/ball logic for y value listener of the ball
     */
    public void animateY() {
        if ((ball.getTranslateY() == HEIGHT)) {
            //Ball hitting the bottom of the screen
            System.out.println("hitting the bottom coming from the left");
            double touchX = ballPath.getEndX();
            double touchY = HEIGHT;
            double nY;
            double nX;

            if (ballPath.getStartX() <= WIDTH / 2) {
                //ball coming from the left
                nX = WIDTH - PLAYER_WIDTH;

            } else {
                //ball coming from the right
                nX = PLAYER_WIDTH;
            }
            nY = rand.nextInt(HEIGHT - 10) + 10;
            ptBall.stop();
            setNewAnimationPath(touchX, touchY, nY, nX);
            ptBall.play();
        }
        if (ball.getTranslateY() == 0) {
            //ball hits the top of the window
            double touchX = ballPath.getEndX();
            double touchY = 0;
            double d = touchX - ballPath.getStartX();
            double nY;
            double nX;

            if (ballPath.getStartX() <= WIDTH / 2) {
                //ball coming from the left
                nX = WIDTH - PLAYER_WIDTH;

            } else {
                //ball coming from the right
                nX = PLAYER_WIDTH;
            }

            nY = rand.nextInt(HEIGHT - 10) + 10;

            ptBall.stop();
            setNewAnimationPath(touchX, touchY, nY, nX);
            ptBall.play();
        }
    }

    private void setNewAnimationPath(double touchX, double touchY, double nY, double nX) {
        // length of the previous path
        speedRate *= 0.999;

        double oldDistance = calcDistance(ballPath.getStartX(), ballPath.getStartY(), ballPath.getEndX(), ballPath.getEndY());

        ptBall.setPath(new Line(touchX, touchY, nX, nY));
        double newDistance = calcDistance(touchX, touchY, nX, nY);
        double oldDuration = ptBall.getDuration().toSeconds();
        double newDuration = (oldDuration / oldDistance * newDistance) * speedRate;
        ptBall.setDuration(Duration.seconds(newDuration));
    }

    /**
     * Keeps the ball animating on the screen and checks if it hits the human paddle and updates scores/lives accordingly
     * Game/ball logic for x value listener of the ball
     */
    public void animateX() {
        if (ball.getTranslateX() == WIDTH - PLAYER_WIDTH) {
            //near the computer paddle
            double touchX = WIDTH - PLAYER_WIDTH;
            double touchY = ballPath.getEndY();
            double nY;
            double nX;


            if (ballPath.getStartY() >= HEIGHT / 2) {
                //hit the computer paddle from the bottom half of the screen
                nY = 0;
            } else {
                //hit the computer paddle from the top half of the screen
                nY = HEIGHT;
            }
            nX = rand.nextInt(WIDTH - 15) + 15;

            ptBall.stop();
            setNewAnimationPath(touchX, touchY, nY, nX);
            ptBall.play();

        }

        if (ball.getTranslateX() == PLAYER_WIDTH) {
            //ball near the human paddle
            double touchX = PLAYER_WIDTH;
            double touchY = ballPath.getEndY();
            double nY;
            double nX;


            if (ballPath.getStartY() >= HEIGHT / 2) {
                //hit the computer paddle from the bottom half of the screen
                nY = 0;
            } else {
                //hit the computer paddle from the top half of the screen
                nY = HEIGHT;
            }
            if (humanPaddle.getY() <= ball.getTranslateY() && ball.getTranslateY() <= (humanPaddle.getY() + PLAYER_HEIGHT)) {
                //if it touches the human paddle
                playerScore++;
                score.setText("" + playerScore);

                nX = rand.nextInt(WIDTH - 15) + 15;

                ptBall.stop();
                setNewAnimationPath(touchX, touchY, nY, nX);
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
                    nX = rand.nextInt(WIDTH - 15) + 15;
                    setNewAnimationPath(touchX, touchY, nY, nX);
                    ptBall.play();
                    typeWordForExtraLife();
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

    /**
     * Saves the scoreboard to a file that will be read and whose data will be eventually displayed in our scoreboard table
     */
    public static void saveScoresToFile(String fileName, ArrayList<Players> list) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
        for (Players play : list) {
            pw.println(play.getName());
            pw.println(play.getScore());
        }
        pw.close();
    }

    /**
     * Reads the current scoreboard from the file and RETURNS an arraylist of current scoreboard based on file data.
     */
    public static ArrayList readScores() throws IOException, ClassNotFoundException {
        ArrayList<Players> board = new ArrayList<>();
        final String INPUT_FILE = "players.txt";
        String name, string_score;
        int score;
        Scanner fileScan, playerScan;
        fileScan = new Scanner(new File(INPUT_FILE));

        while (fileScan.hasNext()) {
            name = fileScan.nextLine();
            string_score = fileScan.nextLine();
            score = Integer.parseInt(string_score);

            board.add(new Players(name, score));
        }
        return board;

    }

    /**
     * Allows the player to redeem a life by typing in a random word out of the given list within a specific time period.
     */
    public void typeWordForExtraLife() {
        Timer t = new Timer();
        String word = generateWord();
        type2AddLives.setText("Type the following word to increase your number of lives! \n The word is: " + word);
        type2AddLives.setFill(Color.WHITE);


        type2AddLives.setVisible(true);
        checkingWord = true;

        long delay = 3000L;// gives 3 seconds to type word
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    checkingWord = false;
                    String typedWord = typed[0];
                    System.out.println(typedWord);

                    if (typedWord.equalsIgnoreCase(word)) {
                        //the word typed matches the generated word
                        System.out.println((typedWord.equalsIgnoreCase(word)));
                        System.out.println(word);
                        lives++;
                        currentLives.setText("" + lives);
                        System.out.println(lives);

                    }
                    type2AddLives.setVisible(false);
                    typed[0] = "";
                    t.cancel();
                });
            }
        }, delay);

    }

    /**
     * Returns a word at random from the set array of words
     */
    public String generateWord() {
        String[] words = {"accuracy", "finesse", "champion", "undefeated", "achievement", "winner", "victorious", "fast-typer"};
        String wordToType = words[rand.nextInt(words.length)];

        return wordToType;

    }
}
