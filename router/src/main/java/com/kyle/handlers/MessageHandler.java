package com.kyle.handlers;

import java.nio.channels.SelectionKey;

import com.kyle.Router;



public abstract class MessageHandler {

    protected MessageHandler nextHandler;
    protected Router router;
  
    protected MessageHandler(Router router){
        this.router = router;
    }

    public void setNextHandler(MessageHandler handler){
        this.nextHandler = handler;
    }

    public void handleMessage(String message, SelectionKey key){
      System.out.println(message);
     }
}
