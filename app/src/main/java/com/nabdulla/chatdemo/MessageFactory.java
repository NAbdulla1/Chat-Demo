package com.nabdulla.chatdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class MessageFactory {
    private List<Message> messages = new ArrayList<>();
    private Random random = new Random();
    private OptionFactory factory = new OptionFactory();
    private int next;

    public MessageFactory() {
        for (int i = 0; i < 20; i++) {
            boolean hasOptions = random.nextBoolean();
            List<Option> options;
            Message message = new Message("Sample Message " + i, true, hasOptions);
            if (hasOptions) {
                options = factory.getOptions(random.nextInt(10));
                message.getOptions().addAll(options);
            }

            messages.add(message);
        }
        next = 0;
    }

    public Message getNextMessage() {
        Message message = messages.get(next);
        next = (next + 1) % messages.size();
        return message;
    }
}
