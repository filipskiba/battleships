package com.battleships;

import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board extends Parent {
    private VBox rows = new VBox();
    private boolean enemy;
    private int ships = 5;
    private HashMap<Cell, Ship> shipCoordinates = new HashMap<>();
    private ArrayList<Cell> securedArea = new ArrayList<>();
    private ArrayList<Cell> usedCells = new ArrayList<>();
    private ArrayList<Ship> shipsList = new ArrayList<>();
    private EventHandler<? super MouseEvent> handler;
    private Image fire = new Image("fire32.png");
    private Image shipImg = new Image("ship32.png");
    private Image missed = new Image("missed32.png");

    public Board(boolean enemy, EventHandler<? super MouseEvent> handler) {
        this.handler = handler;
        this.enemy = enemy;
        createBoard();
    }

    private void createBoard() {
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this);
                c.setOnMouseClicked(this.handler);
                row.getChildren().add(c);
            }
            rows.getChildren().add(row);
        }
        getChildren().add(rows);
    }

    public Cell getCell(int x, int y) {
        return (Cell) ((HBox) rows.getChildren().get(y)).getChildren().get(x);
    }

    public boolean placeShipOnBoard(Ship ship, int x, int y) {
        if (validPlace(ship, x, y)) {
            if (ship.isVertical()) {
                placeVerticalShip(ship, x, y);
                placeVerticalSecuredArea(ship,x,y);
            } else {
                placeHorizontalShip(ship, x, y);
                placeHorizontalSecuredArea(ship,x,y);
            }
            shipsList.add(ship);
            return true;
        }
        return false;
    }

    private void placeVerticalShip(Ship ship, int x, int y) {
        for (int i = y; i < y + ship.getType(); i++) {
            Cell cell = getCell(x, i);
            shipCoordinates.put(cell, ship);
        }
        paintCell();
    }

    private void placeHorizontalShip(Ship ship, int x, int y) {
        for (int i = x; i < x + ship.getType(); i++) {
            Cell cell = getCell(i, y);
            shipCoordinates.put(cell, ship);
        }
        paintCell();
    }

    private void placeHorizontalSecuredArea(Ship ship, int x, int y){
        if(isCellOnBoard(x+ship.getType(),y)) {
            Cell front = getCell(x + ship.getType(), y);
            if(!shipCoordinates.containsKey(front))
            securedArea.add(front);
        }
        if(isCellOnBoard(x-1,y)) {
            Cell back = getCell(x - 1, y);
            if(!shipCoordinates.containsKey(back))
            securedArea.add(back);
        }

        for (int i = x; i < x + ship.getType(); i++) {
            if(isCellOnBoard(i,y+1)) {
                Cell cell = getCell(i, y + 1);
                if(!shipCoordinates.containsKey(cell))
                securedArea.add(cell);
            }
        }
        for (int i = x; i < x + ship.getType(); i++) {
            if(isCellOnBoard(i,y-1)) {
                Cell cell = getCell(i, y -1);
                if(!shipCoordinates.containsKey(cell))
                securedArea.add(cell);
            }
        }
        paintSecuredArea();
    }

    private void placeVerticalSecuredArea(Ship ship, int x, int y){
        if(isCellOnBoard(x,y-1)) {
            Cell back = getCell(x, y - 1);
            if(!shipCoordinates.containsKey(back))
            securedArea.add(back);
        }
        if(isCellOnBoard(x,y+ship.getType())) {
            Cell front = getCell(x, y + ship.getType());
            if(!shipCoordinates.containsKey(front))
            securedArea.add(front);
        }

        for (int i = y; i < y + ship.getType(); i++) {
            if(isCellOnBoard(x+1,i)) {
                Cell cell = getCell(x+1, i);
                if(!shipCoordinates.containsKey(cell))
                securedArea.add(cell);
            }
        }
        for (int i = y; i < y + ship.getType(); i++) {
            if(isCellOnBoard(x-1,i)) {
                Cell cell = getCell(x-1, i);
                if(!shipCoordinates.containsKey(cell))
                securedArea.add(cell);
            }
        }
        paintSecuredArea();

    }

    private boolean isShipNearby(Ship ship, int x, int y){
        if (ship.isVertical()) {
            for (int i = y; i < y + ship.getType(); i++) {
                if (securedArea.contains(new Cell(x, i, this))) {
                    return true;
                }
            }
        } else {
            for (int i = x; i < x + ship.getType(); i++) {
                if (securedArea.contains(new Cell(i, y, this))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isThereAnotherShip(Ship ship, int x, int y) {
        if (ship.isVertical()) {
            for (int i = y; i < y + ship.getType(); i++) {
                if (shipCoordinates.containsKey(new Cell(x, i, this))) {
                    return true;
                }
            }
        } else {
            for (int i = x; i < x + ship.getType(); i++) {
                if (shipCoordinates.containsKey(new Cell(i, y, this))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean validPlace(Ship ship, int x, int y) {
        if (!isThereAnotherShip(ship, x, y) && !isShipNearby(ship,x,y)) {
            if (ship.isVertical())
                return y >= 0 && (y + ship.getType()) - 1 < 10;
            else
                return x >= 0 && (x + ship.getType()) - 1 < 10;
        }
        return false;
    }

    public void paintCell() {
        for (Map.Entry<Cell, Ship> entry : shipCoordinates.entrySet()) {
            if (enemy) {
                Cell cell = getCell(entry.getKey().getPositionX(), entry.getKey().getPositionY());
                cell.setFill(new ImagePattern(shipImg));
            }
        }
    }
    public void paintSecuredArea() {
        for (Cell c: securedArea) {
            if (enemy) {
                Cell cell = c;
                cell.setFill(Color.LIGHTSKYBLUE);
            }
        }
    }

    public double getShipsHealth() {
        double sum = 0;
        double allShipsHealth = 15;
        for (Ship s : shipsList) {
            sum += s.getHealth();
        }
        return sum / allShipsHealth;
    }

    public boolean isCellUsed(int x, int y) {
        Cell cell = getCell(x, y);
        if (usedCells.contains(cell)) {
            return true;
        }
        return false;
    }

    public boolean isCellOnBoard(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public boolean fire(int x, int y) {
        Cell cell = getCell(x, y);
        if (this.shipCoordinates.containsKey(cell)) {
            cell.setFill(new ImagePattern(fire));
            this.shipCoordinates.get(cell).hit();
            this.usedCells.add(cell);
            if (!this.shipCoordinates.get(cell).isAlive()) {
                this.ships--;
            }
            return true;
        } else {
            cell.setFill(new ImagePattern(missed));
        }
        usedCells.add(cell);
        return false;
    }

    public int getShips() {
        return ships;
    }
}
