package com.kyle.userinterface;

import java.util.Scanner;

import com.kyle.Broker;

public class UserInterface implements Runnable {
    private Broker broker;
    private String currentMarket;
    private boolean mainMenu = false;
    private boolean acceptableOption = false;
    private Scanner input = new Scanner(System.in);

    public UserInterface(Broker broker) {
        this.broker = broker;
    }

    @Override
    public void run() {
        try {
            System.out.println("Starting up the user interface in its own thread.");
            Thread.sleep(8000);
            getMarkets();
            start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void getMarkets() {
        broker.sendMessage("Retrieve markets");
    }

    public void getSpecificMarket(String marketId) {
        broker.sendMessage("List good for market:" + marketId + ":" + broker.getId());
    }

    public void start() {
        try {
            while (broker.getMarketsRetrieve() != true) {
                Thread.sleep(2000);
                getMarkets();
            }
            while (mainMenu) {
                while (!acceptableOption) {
                    System.out.println(
                            "Your options are:\n1. Choose a market\n2. Redisplay Markets. \n 3. Show your current cash and inventory");
                    String s = input.nextLine();
                    parseInput(s);
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void parseInput(String input) {
        switch (input) {
            case "1":
                acceptableOption = true;
                chooseMarket();
                break;
            case "2":
                getMarkets();
                break;
            case "3":
                getStats();
                break;
            default:
                System.out.println("Please choose options 1, 2 or 3 only");
                break;
        }
    }

    public void parseMarketInput(String input) {
        input = input.trim();
        getSpecificMarket(input);
    }

    public void chooseMarket() {
        broker.setValidMarket(false);
        while (!broker.getValidMarket()) {
            try {
                System.out.println("Please enter the ID of your market of choice.");
                String s = input.nextLine();
                parseMarketInput(s);
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        // Todo
    }

    public void getStats() {
        // TODO
    }
}
