package com.kyle.handlers;

import com.kyle.Market;

public class InfoMessages extends MessageHandler {
    public InfoMessages(Market market) {
        super(market);
    }

    @Override
    public void handleMessage(String message) {
        if (message.contains("List your instruments for:")) {
            String[] array = message.split(":");
            String brokerId = array[1];

            System.out.println("The router has assigned you an ID. It is " + array[1]
                    + ". Saving the ID for future reference and responding to Router...");
            market.setId(array[1]);
            market.sendMessage("I am Market:" + array[1].trim());
        } else if (nextHandler != null) {
            nextHandler.handleMessage(message);
        }
    }

}
