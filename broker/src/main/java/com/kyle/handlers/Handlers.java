package com.kyle.handlers;

import com.kyle.Broker;

public class Handlers {
    AdminMessages adminMessages;

    public Handlers(Broker broker) {
        setup(broker);
    }

    public void setup(Broker broker) {
        adminMessages = new AdminMessages(broker);
        adminMessages.setNextHandler(null);
    }

    public void handleMessage(String message){
        adminMessages.handleMessage(message);
    }
}
