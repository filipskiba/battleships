package com.battleships;

import javafx.stage.Stage;

import java.util.Objects;

public class Message {
    private String title;
    private String informationText;
    private boolean closeGame;


    public String getTitle() {
        return title;
    }

    public String getInformationText() {
        return informationText;
    }

    public void close(Stage stage) {
        if (!this.closeGame) {
            stage.close();
        } else {
            System.exit(0);
        }
    }

    public Message(String title, String informationText, boolean closeGame) {
        this.title = title;
        this.informationText = informationText;
        this.closeGame = closeGame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return closeGame == message.closeGame &&
                Objects.equals(title, message.title) &&
                Objects.equals(informationText, message.informationText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, informationText, closeGame);
    }
}
