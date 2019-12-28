package com.ntuzy.juc_01;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author IamZY
 * @create 2019/12/28 14:57
 */
public class CallableDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask task = new FutureTask(new MyThread2());
        new Thread(task, "A").start();
        Integer i = (Integer) task.get();
        System.out.println(i);

    }


    static class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }

    static class MyThread1 implements Runnable {

        @Override
        public void run() {

        }
    }


    static class MyThread2 implements Callable<Integer> {

        // 有无返回值
        // 实现Callable
        // 抛出异常
        @Override
        public Integer call() throws Exception {
            System.out.println("come in call method ... ");
            return 1024;
        }
    }


}
