package com.ntuzy.juc_01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 三个售票员 卖30张票
 * <p>
 * 固定的编程套路+模板
 * 在高内聚低耦合的情况下 线程 操作 资源类
 */
public class SaleTickerDemo01 {

    public static void main(String[] args) {

        Tickets tickets = new Tickets();

        String[] str = new String[]{"A", "B", "C"};

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (int j = 0; j < 40; j++) {
                    tickets.sale();
                }
            }, str[i]).start();
        }

//        Thread.State;

    }

    static class Tickets {  // 实例变量+实例方法

        private int num = 30;

        // List list = new ArrayList();

        private Lock lock = new ReentrantLock();


        public void sale() {
            lock.lock();

            try {
                if (num > 0) {
                    System.out.println(Thread.currentThread().getName() + " sell 第" + num + "张，还剩下 " + (--num) + "张");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }


    }

}
