package com.example.study;

public class JustTest {

    public static void print(int num) {
        for (int i = 31; i >= 0; i--) {
            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int a = Integer.MIN_VALUE;
        int b = -a;
        System.out.println(a);
        System.out.println(b);
        print(a);
        print(b);
        System.out.println(a == b);


//        Object obj = new Object();
//        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
//
//        synchronized (obj) {
//            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
//            try {
//                obj.wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }

}
