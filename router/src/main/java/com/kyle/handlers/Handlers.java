package com.kyle.handlers;

import java.nio.channels.SelectionKey;

import com.kyle.Router;

public class Handlers {
    AdminMessages adminMessages;

    public Handlers(Router router) {
        setup(router);
    }

    public void setup(Router router) {
        adminMessages = new AdminMessages(router);
        adminMessages.setNextHandler(null);
    }

    public void handleMessage(String message, SelectionKey key){
        adminMessages.handleMessage(message, key);
    }
}
