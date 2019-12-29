package com.ntuzy.juc_01;

/**
 * @Author IamZY
 * @create 2019/12/29 11:27
 */
public class JVMNote {

    // java.lang.StackOverflowError
    public static void m1 () {
        m1();
    }

    public static void main(String[] args){
        m1();
    }

}
