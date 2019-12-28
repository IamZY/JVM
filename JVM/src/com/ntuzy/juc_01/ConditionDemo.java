package com.ntuzy.juc_01;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author IamZY
 * @create 2019/12/27 17:14
 */
public class ConditionDemo {
    public static void main(String[] args) {

        ShareData shareData = new ShareData();

        new Thread(()->{
            for(int i = 0;i < 10;i++) {
                shareData.print5();
            }
        },"A").start();

        new Thread(()->{
            for(int i = 0;i < 10;i++) {
                shareData.print10();
            }
        },"B").start();

        new Thread(()->{
            for(int i = 0;i < 10;i++) {
                shareData.print15();
            }
        },"C").start();


    }


    static class ShareData {
        private int num = 1;  // A : 1 B : 2 C : 3

        private Lock lock = new ReentrantLock();
        // 一把锁配多把钥匙
        private Condition A = lock.newCondition();
        private Condition B = lock.newCondition();
        private Condition C = lock.newCondition();

        public void print5() {

            lock.lock();

            try {

                while (num != 1) {
                    //
                    A.await();
                }

                // 干活
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }

                // 通知
                num = 2;
                // 如果通知第2个
                B.signal();


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }


        }


        public void print10() {

            lock.lock();

            try {

                while (num != 2) {
                    //
                    B.await();
                }

                // 干活
                for (int i = 0; i < 10; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }

                // 通知
                num = 3;
                // 如果通知第2个
                C.signal();


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }


        }


        public void print15() {

            lock.lock();

            try {

                while (num != 3) {
                    //
                    C.await();
                }

                // 干活
                for (int i = 0; i < 15; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + i);
                }

                // 通知
                num = 1;
                // 如果通知第2个
                A.signal();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }


        }



    }

}
