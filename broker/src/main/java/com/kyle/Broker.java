package com.kyle;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

import javax.sound.midi.SysexMessage;

import com.kyle.handlers.Handlers;
import com.kyle.handlers.sendMessage;

public class Broker {
   private static BufferedReader input = null;
   private Handlers messageHandlers = new Handlers(this);
   private String Id = null;
   private boolean messageReady = false;
   private String brokerMessage = null;
   private Selector selector = null;
   private SocketChannel sc = null;

   public Broker() {
      // TODO
   }

   public void mainBroker() throws Exception {
      // try {
      System.out.println(
            "Hello, I am your friendly Broker. Please wait a moment while I set things up and connect to the Router");
      socketSetup();
      checkSelector();
      // } catch (IOException e) {
      // System.out.println(e);
      // } finally {
      // if (sc != null) {
      // sc.close();
      // }
      // if (selector != null) {
      // selector.close();
      // }
      // }
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
            System.out.println("I have successfully connected with the router. Waiting to be assigned a broker ID...");
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

   public void setId(String id) {
      Id = id;
   }

   public String getId() {
      return Id;
   }

   public SocketChannel getSc() {
      return sc;
   }

   public void sendMessage(String msg) {
      ByteBuffer bb = ByteBuffer.wrap(msg.getBytes());
      try {
         sc.write(bb);
      } catch (Exception e) {
         System.out.println(e);
      }
   }
}