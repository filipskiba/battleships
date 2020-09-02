package com.battleships;

import javafx.scene.Parent;

import java.util.Objects;

public class Ship extends Parent {
    private int type;
    private boolean vertical;

    private int health;

    public Ship(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = type;
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getHealth() {
        return health;
    }

    public int getType() {
        return type;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return type == ship.type &&
                vertical == ship.vertical &&
                health == ship.health;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "type=" + type +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, vertical, health);
    }
}
