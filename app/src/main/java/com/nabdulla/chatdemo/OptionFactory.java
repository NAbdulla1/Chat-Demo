package com.nabdulla.chatdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class OptionFactory {
    private List<Option> options = new ArrayList<>();
    private Random random = new Random();

    public OptionFactory() {
        for (int i = 0; i < 100; i++) {
            String name = "Option " + i;
            String id = "option-" + i;
            options.add(new Option(name, id));
        }
    }

    public List<Option> getOptions(int count) {
        int st = random.nextInt(options.size() - 2);
        return options.subList(st, Math.min(options.size() - 1, st + count));
    }
}
