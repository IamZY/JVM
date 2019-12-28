# JVM

[toc]

## JUC

`java.util.concurrent`

+ java.util.concurrent
+ java.util.locks
+ java.util.atomic

### 进程/线程

### 并发/并行

## 线程

```java
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
```

[image]()

## 类装载器

负责加载class文件 class文件在**文件开头有特定的文件标识**，将class文件字节码内容加载到内存中，并将这些内容转换成方法去中运行时数据结构并且ClassLoader只负责class文件的加载，至于它是否可以运行，则由Execution Engine决定。

> cafe babe

### 虚拟机自带的加载器

![image-20191228154656126](D:\src\JVM\images\image-20191228154656126.png)

+ 虚拟机自带加载器  bootstarp C++
+ 扩展类加载器  Extension  Java   | jre-lib-ext
+ 应用程序类加载器 AppClassLoader

```java
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

```

### 用户自定义加载器

Java.lang.ClassLoader

## 方法区

## Java栈

## 本地方法栈





























