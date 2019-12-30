package com.ntuzy.juc_01;

import java.util.Random;

/**
 * @Author IamZY
 * @create 2019/12/29 11:27
 */
public class JVMNote {

    // java.lang.StackOverflowError
    public static void m1() {
        m1();
    }

    public static void main(String[] args) {
//        m1();

        String str = "www.baidu.com";
        while (true) {
            str += str + new Random().nextInt(88888888) + new Random().nextInt(999999999);
        }


    }

}
