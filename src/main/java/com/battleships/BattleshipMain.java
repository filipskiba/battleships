package com.battleships;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Random;

public class BattleshipMain extends Application {
    private Board enemyBoard, playerBoard;
    private int shipsToPlace = 5;
    private Random random = new Random();
    private boolean enemyTurn;
    private boolean isGameStarted;
    private ProgressBar p1 = new ProgressBar();
    private ProgressBar p2 = new ProgressBar();
    private MessageList messageList = new MessageList();

    private Parent createContent() {
        p1.setProgress(1);
        p2.setProgress(1);
        p1.setPrefSize(400, 15);
        p2.setPrefSize(400, 15);
        createMessages();


        playerBoard = new Board(true, event -> {
            playerGameController(event);
        });
        enemyBoard = new Board(false, event -> {
            enemyGameController(event);
        });

        Label player1 = new Label("Player:");
        player1.setFont(Font.font(30));

        Label player2 = new Label("Enemy:");
        player2.setFont(Font.font(30));


        HBox title = new HBox(100, player1, p1, player2, p2);
        title.setAlignment(Pos.BASELINE_CENTER);
        VBox left = new VBox(10, player1, p1, playerBoard);
        VBox right = new VBox(10, player2, p2, enemyBoard);
        HBox root = new HBox(10, left, right);
        root.setPrefSize(800, 600);
        openMessageWindow(messageList.getMessages().get("startMessage"));
        return root;

    }

    private void createMessages() {
        Message startGameMessage = new Message("Place you'r ships", "Place five ships on your board.\n " +
                "Click left mouse button to add ship horizontal or\n" +
                "click right mouse button to add ship vertically.", false);
        Message moveMessage = new Message("Player move", "Click left mouse button on enemy board to shot enemy ship", false);
        Message playerWinMessage = new Message("Player win", "You win!", true);
        Message playerLoseMessage = new Message("Enemy win", "You lose!", true);
        Message badShipLocation = new Message("Wrong ship location", "Can not place ship here", false);

        messageList.addMessage("startMessage", startGameMessage);
        messageList.addMessage("moveMessage", moveMessage);
        messageList.addMessage("winMessage", playerWinMessage);
        messageList.addMessage("loseMessage", playerLoseMessage);
        messageList.addMessage("badLocationMessage", badShipLocation);
    }

    private void playerGameController(MouseEvent event) {
        if (!isGameStarted) {
            Cell cell = (Cell) event.getSource();
            placePlayerShip(cell, event.getButton() == MouseButton.PRIMARY);
        }
    }

    private void enemyGameController(MouseEvent event) {
        if (!isGameStarted)
            return;

        Cell cell = (Cell) event.getSource();
        Shoot(cell);
        if (enemyBoard.getShips() == 0) {
            openMessageWindow(messageList.getMessages().get("winMessage"));
            isGameStarted = false;
        }
        if (enemyTurn) {
            enemyMove();
        }
    }

    private void Shoot(Cell cell) {
        if (!enemyBoard.isCellUsed(cell.getPositionX(), cell.getPositionY())) {
            if (!enemyBoard.fire(cell.getPositionX(), cell.getPositionY())) {
                enemyTurn = true;
            }
            getHealth();
        }
    }

    private void getHealth() {
        p2.setProgress(enemyBoard.getShipsHealth());
        p1.setProgress(playerBoard.getShipsHealth());
    }

    private void openMessageWindow(Message message) {
        TextArea messageText = new TextArea();
        messageText.setText(message.getInformationText());
        messageText.setFont(Font.font(25));
        messageText.setEditable(false);

        Button button = new Button();
        button.setText("Ok");
        button.setPrefSize(80, 200);

        VBox buttonMenu = new VBox(15, button);
        buttonMenu.setPadding(new Insets(10, 10, 10, 10));
        buttonMenu.setAlignment(Pos.TOP_RIGHT);

        VBox messageLayout = new VBox(10, messageText, buttonMenu);
        messageLayout.setAlignment(Pos.TOP_CENTER);
        Scene messageScene = new Scene(messageLayout, 800, 200);
        // New window (Stage)
        Stage messageWindow = new Stage();
        messageWindow.setAlwaysOnTop(true);
        messageWindow.setTitle(message.getTitle());
        messageWindow.setScene(messageScene);
        messageWindow.initModality(Modality.WINDOW_MODAL);
        messageWindow.show();
        button.setOnAction(event -> message.close(messageWindow));
    }

    public void placePlayerShip(Cell cell, boolean vertical) {
        if (playerBoard.placeShipOnBoard(new Ship(shipsToPlace, vertical), cell.getPositionX(), cell.getPositionY())) {
            --shipsToPlace;
            if (shipsToPlace == 0) {
                placeEnemyShips();
                openMessageWindow(messageList.getMessages().get("moveMessage"));
            }
        } else {
            openMessageWindow(messageList.getMessages().get("badLocationMessage"));
        }
    }

    public void placeEnemyShips() {
        int type = 5;

        while (type > 0) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            if (enemyBoard.placeShipOnBoard(new Ship(type, Math.random() < 0.5), x, y)) {
                type--;
            }
        }
        isGameStarted = true;
    }

    private void enemyMove() {
        while (enemyTurn) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            if (!playerBoard.isCellUsed(x, y)) {
                if (playerBoard.fire(x, y))
                    continue;
                enemyTurn = playerBoard.fire(x, y);
            }
            if (playerBoard.getShips() == 0) {
                openMessageWindow(messageList.getMessages().get("loseMessage"));
            }
            getHealth();
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Battleships");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
