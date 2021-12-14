package com.example.study.juc.ordering;

/**
 *
 */
public class NoVisibility {

    private static boolean ready = false;
    private static int number;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready) {
                Thread.yield();
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) throws Exception {
        Thread t = new ReaderThread();
        t.start();
        number = 42;
        ready = true;
        t.join();
    }

}
