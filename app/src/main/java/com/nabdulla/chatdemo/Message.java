package com.nabdulla.chatdemo;

import java.util.ArrayList;
import java.util.List;

public class Message implements IMessage {
    private String message;
    private boolean botMessage;
    private boolean disableMessageBox;
    private List<Option> options;

    public Message(String message, boolean botMessage, boolean disableMessageBox) {
        this.message = message;
        this.botMessage = botMessage;
        this.disableMessageBox = disableMessageBox;
        this.options = new ArrayList<>();
    }

    public Message() {
        this(null, false, false);
    }

    @Override
    public boolean isBotMessage() {
        return botMessage;
    }

    @Override
    public boolean isDisableMessageBox() {
        return disableMessageBox;
    }


    @Override
    public boolean hasOptions() {
        return options.size() != 0;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBotMessage(boolean botMessage) {
        this.botMessage = botMessage;
    }

    public List<Option> getOptions() {
        return options;
    }
}
