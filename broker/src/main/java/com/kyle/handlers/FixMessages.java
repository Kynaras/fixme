package com.kyle.handlers;

import java.nio.channels.SelectionKey;

import com.kyle.Broker;

public class FixMessages extends MessageHandler {

    public FixMessages(Broker broker) {
        super(broker);
    }

    // "49=1|8=FIX.4.4|35=A|9=85|34=1|52=2020-12-14T08:22:00.163260700|56=333555|40=1|54=1|55=ZAR|38=10|58=CORN|44=20|10=110"

    public boolean checkSumCheck(String msgbody){
        String [] array = msgbody.split("10=");
        String [] checksumOriginal = array[1].split("\\|");
        String checksum = broker.getFix().checksumGen(array[0]);
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
            if(!checkSumCheck(message))
            {
                return;
            }
            String[] array = message.split("\\|");
            String brokerId = array[6].split("=")[1];
            String type = array[8].split("=")[1];
            String price = array[12].split("=")[1];
            String qty = array[10].split("=")[1];
            String instrument = array[11].split("=")[1];
            String checksum = array[13].split("=")[1];
           
        }

    }
}
