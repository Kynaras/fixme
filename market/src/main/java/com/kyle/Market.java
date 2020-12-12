package com.kyle;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

import com.kyle.handlers.Handlers;

public class Market {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/fixme";

    static final String USER = "root";
    static final String PASS = "admin";

    private static BufferedReader input = null;
    private Handlers messageHandlers = new Handlers(this);
    private String Id = null;
    private boolean messageReady = false;
    private String brokerMessage = null;
    private Selector selector = null;
    private SocketChannel sc = null;

    public static void main(String[] args) {
        Market market = new Market();
        market.mainMarket();
    }

    public Market() {
        // TODO
    }

    public void mainMarket() {
        // try {
        System.out.println(
                "Hello, I am your friendly Market. Please wait a moment while I set things up and connect to the Router");
        socketSetup();
        checkSelector();
    }

    public Boolean processKeys(Set readySet) throws Exception {
        SelectionKey key = null;
        Iterator iterator = null;
        iterator = readySet.iterator();
        while (iterator.hasNext()) {
            key = (SelectionKey) iterator.next();
            iterator.remove();
        }
        if (key.isConnectable()) {
            Boolean connected = serverConnect(key);
            if (!connected) {
                return true;
            } else {
                System.out.println(
                        "I have successfully connected with the router. Waiting to be assigned a market ID...");
                key.interestOps(SelectionKey.OP_READ);
                checkSelector();
            }
        }
        if (key.isReadable()) {
            ByteBuffer bb = ByteBuffer.allocate(1024);
            sc.read(bb);
            String result = new String(bb.array()).trim();
            messageHandlers.handleMessage(result);
            System.out.println("Message received from Router: " + result + " Message length= " + result.length());
            checkSelector();
        }

        if (this.messageReady) {
            String msg = brokerMessage;
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer bb = ByteBuffer.wrap(msg.getBytes());
            channel.write(bb);

        }

        // Comment this out for now. Maybe have a boolean check for message status
        // instead
        // if (key.isWritable()) {
        // System.out.print("Type a message (type quit to stop): ");
        // String msg = input.readLine();
        // if (msg.equalsIgnoreCase("quit")) {
        // return true;
        // }
        // SocketChannel sc = (SocketChannel) key.channel();
        // ByteBuffer bb = ByteBuffer.wrap(msg.getBytes());
        // sc.write(bb);
        // }
        return false;
    }

    public Boolean serverConnect(SelectionKey key) {
        SocketChannel sc = (SocketChannel) key.channel();
        try {
            while (sc.isConnectionPending()) {
                sc.finishConnect();
            }
        } catch (IOException e) {
            key.cancel();
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void checkSelector() {
        try {
            // while (true) {
            if (selector.select() > 0) {
                Boolean doneStatus = processKeys(selector.selectedKeys());
                // if (doneStatus) {
                // break;
                // }
                // }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void socketSetup() {
        try {
            InetSocketAddress addr = new InetSocketAddress(InetAddress.getByName("localhost"), 5000);
            selector = Selector.open();
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            sc.connect(addr);
            sc.register(selector, SelectionKey.OP_CONNECT);
            input = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.out.println("Error! " + e);
        }
    }

    public void sendMessage(String msg) {
        ByteBuffer bb = ByteBuffer.wrap(msg.getBytes());
        try {
           sc.write(bb);
        } catch (Exception e) {
           System.out.println(e);
        }
     }

    public SocketChannel getSc() {
        return sc;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    // public void getInstruments() {
    // Connection conn = null;
    // Statement stmt = null;
    // String sql;
    // try {
    // System.out.println("Connecting to database...");
    // conn = DriverManager.getConnection(DB_URL, USER, PASS);

    // System.out.println("Creating statement...");
    // stmt = conn.createStatement();
    // sql = "SELECT * FROM instruments";
    // ResultSet rs = stmt.executeQuery(sql);

    // while (rs.next()) {
    // // Retrieve by column name
    // String type = rs.getString("type");
    // int quantity = rs.getInt("quantity");
    // int price = rs.getInt("price");

    // // Display values
    // System.out.print("type: " + type);
    // System.out.print(", quantity: " + quantity);
    // System.out.print(", price: " + price);
    // // rs.close();
    // // stmt.close();
    // // conn.close();
    // }
    // } catch (Exception e) {
    // System.out.println(e);
    // } finally {
    // // finally block used to close resources
    // try {
    // if (stmt != null)
    // stmt.close();
    // } catch (SQLException se2) {
    // } // nothing we can do
    // try {
    // if (conn != null)
    // conn.close();
    // } catch (SQLException se) {
    // System.out.println(se);
    // }
    // }
    // }
}
