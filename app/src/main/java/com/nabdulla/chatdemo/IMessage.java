package com.nabdulla.chatdemo;

public interface IMessage {
    boolean isBotMessage();

    boolean isDisableMessageBox();

    boolean hasOptions();
}
