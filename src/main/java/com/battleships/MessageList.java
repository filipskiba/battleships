package com.battleships;

import com.battleships.Message;

import java.util.HashMap;

public class MessageList {
    private HashMap<String, Message> tips = new HashMap<>();
    public void addMessage(String name, Message message){
        this.tips.put(name,message);
    }
    public HashMap<String,Message> getMessages(){
        return this.tips;
    }

}
