package com.ntuzy.juc_01;

import com.sun.org.apache.regexp.internal.RE;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author IamZY
 * @create 2019/12/27 16:18
 */
public class ProdConsumerDemo04 {

    public static void main(String[] args) throws Exception {

        AirCondition airCondition = new AirCondition();

//        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                try {
//                    airCondition.increase();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "A").start();
//
//        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                try {
//                    airCondition.decrease();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "B").start();
//
//        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                try {
//                    airCondition.increase();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "C").start();
//
//        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                try {
//                    airCondition.decrease();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, "D").start();


        new Thread(()->{
            for(int i = 0;i < 10;i++) {
                airCondition.increase();
            }
        },"A").start();

        new Thread(()->{
            for(int i = 0;i < 10;i++) {
                airCondition.decrease();
            }
        },"B").start();

        new Thread(()->{
            for(int i = 0;i < 10;i++) {
                airCondition.increase();
            }
        },"C").start();

        new Thread(()->{
            for(int i = 0;i < 10;i++) {
                airCondition.decrease();
            }
        },"D").start();


    }


    static class AirCondition {
        private int number = 0;

//        public synchronized void increase() throws InterruptedException {
//
//            // 虚假唤醒
//            while (number != 0) {
//                this.wait();
//            }
//
//            number++;
//            System.out.println(Thread.currentThread().getName() + "\t" + number);
//            this.notifyAll();
//        }
//
//        public synchronized void decrease() throws InterruptedException {
//
//            while (number == 0) {
//                this.wait();
//            }
//
//            number--;
//            System.out.println(Thread.currentThread().getName() + "\t" + number);
//            this.notifyAll();
//        }


        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();

        public void increase()  {
            lock.lock();

            try {

                while (number != 0) {
                    condition.await();
                }

                number++;
                System.out.println(Thread.currentThread().getName() + "\t" + number);
                condition.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }


        public void decrease() {
            lock.lock();

            try {

                while (number == 0) {
                    condition.await();
                }

                number--;
                System.out.println(Thread.currentThread().getName() + "\t" + number);
                condition.signalAll();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }


    }

}
