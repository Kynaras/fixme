package com.kyle;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class BrokerSocket {

    private SocketChannel sc;
    private Selector selector;
    private String id;

    public BrokerSocket(String id, SocketChannel sc) {
        this.sc = sc;
        this.id = id;
        try {
            this.selector = Selector.open();
        } catch (Exception e) {
            System.out.println("Broker thread error: " + e);
        }
    }

}
