package com.kyle;

public class BrokerUserInterface {
    public static void main(String[] args) {

        try {
            Broker broker = new Broker();
            broker.mainBroker();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
