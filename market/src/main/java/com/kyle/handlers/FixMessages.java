package com.kyle.handlers;

import java.nio.channels.SelectionKey;

import com.kyle.Market;

public class FixMessages extends MessageHandler {

    public FixMessages(Market market) {
        super(market);
    }

    // "49=1|8=FIX.4.4|35=A|9=85|34=1|52=2020-12-14T08:22:00.163260700|56=333555|40=1|54=1|55=ZAR|38=10|58=CORN|44=20|10=110"

    public boolean checkSumCheck(String msgbody){
        String [] array = msgbody.split("10=");
        String [] checksumOriginal = array[1].split("\\|");
        String checksum = market.getFix().checksumGen(array[0]);
        System.out.println(checksumOriginal[0]);
        System.out.println(checksum);
        if (checksum.equals(checksumOriginal[0])) {
            System.out.println("FIX message checksum was successfully validated");
            return true;
        }
        System.out.println("The FIX message checksum was unable to be validated");
        return false;
    }

    @Override
    public void handleMessage(String message) {
        if (message.contains("8=FIX.4.4")) {
            if (!checkSumCheck(message)){
                return;
            }
            String[] array = message.split("\\|");
            String brokerId = array[0].split("=")[1];
            String type = array[8].split("=")[1];
            String price = array[12].split("=")[1];
            String qty = array[10].split("=")[1];
            String instrument = array[11].split("=")[1];
            String checksum = array[13].split("=")[1];
            if (type.contains("2")) {
                market.getDb().sellInstrument(instrument, qty, price, brokerId);
                // } else if (router.getBrokers().containsKey(id)) {
                // router.sendMessage(message, router.getBrokers().get(id));
                // } else {
                // System.out.println("Id provided in the FIX message does not exist!");
                // }
            } else if (type.contains("1")) {
                System.out.println("Buying...");
                if(market.getDb().checkBuyPossible(instrument, qty, price, brokerId)){
                    System.out.println("Bought!");
                }
                market.getDb().buyInstrument(instrument, qty, price, brokerId);
            } else if (nextHandler != null) {
                nextHandler.handleMessage(message);
            }
        }

    }
}
