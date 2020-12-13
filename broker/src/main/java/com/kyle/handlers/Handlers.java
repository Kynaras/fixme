package com.kyle.handlers;

import com.kyle.Broker;

public class Handlers {
    AdminMessages adminMessages;
    InfoMessages infoMessages;

    public Handlers(Broker broker) {
        setup(broker);
    }

    public void setup(Broker broker) {
        adminMessages = new AdminMessages(broker);
        infoMessages = new InfoMessages(broker);
        adminMessages.setNextHandler(infoMessages);
        infoMessages.setNextHandler(null);
    }

    public void handleMessage(String message){
        adminMessages.handleMessage(message);
    }
}
