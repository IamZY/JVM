package com.ntuzy.juc_01;

/**
 * JMM 可见性
 *
 * @Author IamZY
 * @create 2019/12/30 14:35
 */


public class T3 {
    static class MyNumber {
        volatile int number = 10;

        public void addTo1205() {
            this.number = 1205;
        }
    }


    public static void main(String[] args) {
        MyNumber number = new MyNumber();

        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            number.addTo1205();  // 将10修改成1205
            System.out.println(Thread.currentThread().getName() + "\t" + number.number);
        }, "AAA").start();

        while (number.number == 10) {
            // 需要有通知机制告诉main线程 跳出while
//            System.out.println("-----------------------------");
        }

        System.out.println(Thread.currentThread().getName() + "\t" + number.number);

    }

}
