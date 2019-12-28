package com.ntuzy.juc_01;

/**
 * @Author IamZY
 * @create 2019/12/28 15:36
 */
public class MyObject {
    public static void main(String[] args){
        MyObject myObject = new MyObject();
        
        System.out.println(myObject.getClass().getClassLoader().getParent().getParent()); // null bootstrap
        System.out.println(myObject.getClass().getClassLoader().getParent());  // ext
        System.out.println(myObject.getClass().getClassLoader());   // Launcher$AppClassLoader
        
        
        System.out.println("----------------------------------------------------");

        Object o = new Object();
        System.out.println(o.getClass().getClassLoader());  // null  jdk自带  bootstrap
        
        
    }
}
