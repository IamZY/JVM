package com.ntuzy.juc_01;

/**
 * @Author IamZY
 * @create 2019/12/29 16:22
 */
public class T2 {

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());

        long maxMemory = Runtime.getRuntime().maxMemory();//返回 Java 虚拟机试图使用的最大内存量。
        long totalMemory = Runtime.getRuntime().totalMemory();//返回 Java 虚拟机中的内存总量。
        System.out.println("-Xmx:MAX_MEMORY = " + maxMemory + "（字节）、" + (maxMemory / (double) 1024 / 1024) + "MB");  // 堆内存最大
        System.out.println("-Xms:TOTAL_MEMORY = " + totalMemory + "（字节）、" + (totalMemory / (double) 1024 / 1024) + "MB");  // 堆内存最初大小
    }

}


