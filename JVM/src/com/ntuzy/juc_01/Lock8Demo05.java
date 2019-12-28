package com.ntuzy.juc_01;

import java.util.concurrent.TimeUnit;

/**
 *  标准访问
 *      一个对象里面如果有多个syn方法 某一个时刻内 只要一个线程调用其中的一个syn方法 其他的线程只能等待
 *      换句话说在某个时刻内只能由唯一一个线程去访问syn方法 这时候锁的是当前对象this
 *  static syn
 *      类.class 锁的是当前类的Class对象
 *
 * @Author IamZY
 * @create 2019/12/27 15:33
 */
public class Lock8Demo05 {
    public static void main(String[] args) {

        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(()->{
            try {
                phone.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"A").start();

        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(()->{
            try {
//                phone.sendSMS();
//                phone.sayHello();
//                phone2.sendSMS();
//                phone.sendSMS();
//                phone2.sendSMS();
//                phone.sendSMS();
                phone2.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        },"B").start();


    }


    static class Phone {
        public static synchronized void sendEmail() throws Exception {
            TimeUnit.SECONDS.sleep(4);
            System.out.println("sendEmail...");
        }

        public /*static*/ synchronized void sendSMS() throws Exception {
            System.out.println("sendSMS...");
        }
        
        public void sayHello() throws Exception {
            System.out.println("sayHello...");
        }
        
    }

}
