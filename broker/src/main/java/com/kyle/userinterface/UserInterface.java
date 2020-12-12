package com.kyle.userinterface;

import java.util.concurrent.TimeUnit;

import com.kyle.Broker;

public class UserInterface implements Runnable {
    Broker broker;
    String currentMarket;

    public UserInterface(Broker broker) {
        this.broker = broker;
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting up the user interface in its own thread.");
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println(e);
        }
        getMarkets();

    }

    public void getMarkets() {
        broker.sendMessage("Retrieve markets");
    }
}
