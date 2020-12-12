package com.kyle.handlers;

import java.nio.channels.SocketChannel;

import com.kyle.Market;

public abstract class MessageHandler {

    protected MessageHandler nextHandler;
    protected Market market;
    protected SocketChannel sc;
    protected MessageHandler(Market market){
        this.market = market;
        this.sc = market.getSc();
    }

    public void setNextHandler(MessageHandler handler){
        this.nextHandler = handler;
    }

    public void handleMessage(String message){
      System.out.println(message);
     }
}
