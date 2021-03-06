package com.kyle;

import java.net.*;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.*;

import com.kyle.handlers.Handlers;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.nio.ByteBuffer;
import java.nio.channels.*;

import java.io.*;

public class Router {

    private Fix fix= new Fix(this);
    private Handlers messageHandlers = new Handlers(this);
    Selector selector;
    ServerSocketChannel serverSocketChannel;
    ServerSocketChannel serverSocketChannel2;
    SelectionKey key = null;
    SocketChannel errorSc;
    idGen idGen = new idGen();
    Map<String, SelectionKey> brokers = new HashMap<>();
    Map<String, SelectionKey> markets = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Router router = new Router();
        router.startServer();
    }

    public Router() {
        setUp();
    }

    private void setUp() {
        try {
            this.selector = Selector.open();
            this.serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            InetAddress ip = InetAddress.getByName("localhost");
            serverSocketChannel.bind(new InetSocketAddress(ip, 5000));
            // int ops = serverSocketChannel.validOps();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            this.serverSocketChannel2 = ServerSocketChannel.open();
            serverSocketChannel2.configureBlocking(false);
            serverSocketChannel2.bind(new InetSocketAddress(ip, 5001));
            serverSocketChannel2.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void startServer() {
        while (true) {
            // try {
                
            //     Thread.sleep(2000);
            // } catch (Exception e) {
            //     //TODO: handle exception
            // }
            try {
                if (brokers != null) {
                    // Iterator<Map.Entry<String, SelectionKey>> iterator =
                    // brokers.entrySet().iterator();
                    // while (iterator.hasNext()) {
                    // Map.Entry<String, SelectionKey> entry = iterator.next();
                    // System.out.println(entry.getKey() + ":" + entry.getValue());
                    // }
                    // Iterator<Map.Entry<String, SelectionKey>> iteratora =
                    // markets.entrySet().iterator();
                    // while (iteratora.hasNext()) {
                    // Map.Entry<String, SelectionKey> entry = iteratora.next();
                    // System.out.println(entry.getKey() + ":" + entry.getValue());
                    // }

                }
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    errorSc = null;
                    if (key.isAcceptable()) {
                        int id = idGen.generateId();
                        SocketChannel sc = this.serverSocketChannel.accept();
                        if (sc == null) {
                            sc = this.serverSocketChannel2.accept();
                        }
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        System.out.println("Connection Accepted: " + sc.getLocalAddress() + "\n");
                        String msgBody = "Your unique id is: " + id;
                        ByteBuffer bb = ByteBuffer.wrap(msgBody.getBytes());
                        sc.write(bb);
                    }
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        errorSc = sc;
                        ByteBuffer bb = ByteBuffer.allocate(1024);
                        sc.read(bb);
                        String result = new String(bb.array()).trim();
                        System.out.println("Message received: " + result);
                        messageHandlers.handleMessage(result, key);
                        if (result.length() <= 0) {
                            sc.close();
                            System.out.println("Connection closed...");
                            System.out.println("Server will keep running. " + "Try running another client to "
                                    + "re-establish connection");
                        }
                    }
                }
            } catch (IOException err) {
                if (errorSc != null) {
                    // String id;
                    // if (brokers.getKey(key) != null){
                    //     System.out.println("A broker has disconnected. Removing them from the broker table");
                    //     id = brokers.getKey(key);
                    //     brokers.remove(id);
                    // } else if (markets.getKey(key) != null) {
                    //     System.out.println("A market has disconnect. Removing it from the market table");
                    //     id = markets.getKey(key);
                    //     markets.remove(id);
                    // }
                    key.cancel();
                    try {
                        errorSc.socket().close();  
                        errorSc.close(); 
                         
                    } catch (Exception e) {
                       System.out.println("Inner exception" + e);
                    }
                }
                System.out.println(err);
            }
        }
    }

    public void sendMessage(String message, SelectionKey key) {

        try {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer bb = ByteBuffer.wrap(message.getBytes());
            sc.write(bb);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Map<String, SelectionKey> getBrokers() {
        return brokers;
    }

    public void addBroker(String id, SelectionKey key) {
        brokers.put(id, key);
    }

    public void addMarket(String id, SelectionKey key) {
        markets.put(id, key);
    }

    public Map<String, SelectionKey> getMarkets() {
        return markets;
    }

    public Fix getFix() {
        return fix;
    }

    public void setFix(Fix fix) {
        this.fix = fix;
    }
}
