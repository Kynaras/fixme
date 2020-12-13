package com.kyle.handlers;

import com.kyle.Market;

public class Handlers {
    AdminMessages adminMessages;
    InfoMessages infoMessages;

    public Handlers(Market market) {
        setup(market);
    }

    public void setup(Market market) {
        adminMessages = new AdminMessages(market);
        infoMessages = new InfoMessages(market);
        adminMessages.setNextHandler(infoMessages);
        infoMessages.setNextHandler(null);
    }

    public void handleMessage(String message){
        adminMessages.handleMessage(message);
    }
}
