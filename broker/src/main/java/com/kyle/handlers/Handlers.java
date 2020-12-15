package com.kyle.handlers;

import com.kyle.Broker;

public class Handlers {
    AdminMessages adminMessages;
    InfoMessages infoMessages;
    FixMessages fixMessages;

    public Handlers(Broker broker) {
        setup(broker);
    }

    public void setup(Broker broker) {
        adminMessages = new AdminMessages(broker);
        infoMessages = new InfoMessages(broker);
        fixMessages = new FixMessages(broker);
        adminMessages.setNextHandler(infoMessages);
        infoMessages.setNextHandler(fixMessages);
        fixMessages.setNextHandler(null);
    }

    public void handleMessage(String message){
        adminMessages.handleMessage(message);
    }
}
