package com.kyle.handlers;

import java.nio.channels.SelectionKey;

import com.kyle.Router;

public class FixMessages extends MessageHandler {

    public FixMessages(Router router) {
        super(router);
    }

    @Override
    public void handleMessage(String message, SelectionKey key) {
        if (message.contains("8=FIX.4.4")) {
            String [] array = message.split("|");
            String id = array[4];
            if (router.getMarkets().containsKey(id)){
                router.sendMessage(message, router.getMarkets().get(id));
            } else if (router.getBrokers().containsKey(id)) {
                router.sendMessage(message, router.getBrokers().get(id));
            } else {
                System.out.println("Id provided in the FIX message does not exist!");
            }
        } else if (nextHandler != null) {
            nextHandler.handleMessage(message, key);
        }
    }

}
