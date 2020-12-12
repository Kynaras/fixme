package com.kyle;

import java.util.concurrent.TimeUnit;

public class test {
   int test = 1;

    public static void main(String[] args) {
        test test = new test();
    }

    public test() {

        System.out.println(test);
        App app = new App(test);
        System.out.println(test);
        App appu = new App(test);
        System.out.println(test);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        System.out.println("result is" + test);

    }
}
