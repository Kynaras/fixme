package com.kyle.handlers;

import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Map;

import com.kyle.Router;

public class InfoMessages extends MessageHandler {
    public InfoMessages(Router router) {
        super(router);
    }

    @Override
    public void handleMessage(String message, SelectionKey key) {
        if (message.contains("Retrieve markets")) {
            if (router.getMarkets().size() <= 0) {
                router.sendMessage("No markets present", key);
            } else {
                String msg = "These are the markets currently available:\n";
                Iterator<Map.Entry<String, SelectionKey>> iterator = router.getMarkets().entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, SelectionKey> entry = iterator.next();
                        msg += entry.getKey();
                        msg += " market\n";
                    }
                    System.out.println("Gotcha!");
                    router.sendMessage(msg, key);
            }
        }
    }
}
