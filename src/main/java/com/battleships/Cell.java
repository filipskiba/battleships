package com.battleships;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class Cell extends Rectangle {

    private int positionX;
    private int positionY;
    private Board board;

    public Cell(int positionX, int positionY, Board board) {
        super(40, 50);
        this.positionX = positionX;
        this.positionY = positionY;
        this.board = board;
        setFill(Color.LIGHTSEAGREEN);
        setStroke(Color.BLACK);
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return positionX == cell.positionX &&
                positionY == cell.positionY &&
                Objects.equals(board, cell.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionX, positionY, board);
    }
}
