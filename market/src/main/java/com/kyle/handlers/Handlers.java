package com.kyle.handlers;

import com.kyle.Market;

public class Handlers {
    AdminMessages adminMessages;
    InfoMessages infoMessages;
    FixMessages fixMessages;

    public Handlers(Market market) {
        setup(market);
    }

    public void setup(Market market) {
        adminMessages = new AdminMessages(market);
        infoMessages = new InfoMessages(market);
        fixMessages = new FixMessages(market);
        adminMessages.setNextHandler(infoMessages);
        infoMessages.setNextHandler(fixMessages);
        fixMessages.setNextHandler(null);
    }

    public void handleMessage(String message){
        adminMessages.handleMessage(message);
    }
}
