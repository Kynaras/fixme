package com.kyle.handlers;

import com.kyle.Market;

public class Handlers {
    AdminMessages adminMessages;

    public Handlers(Market market) {
        setup(market);
    }

    public void setup(Market market) {
        adminMessages = new AdminMessages(market);
        adminMessages.setNextHandler(null);
    }

    public void handleMessage(String message){
        adminMessages.handleMessage(message);
    }
}
