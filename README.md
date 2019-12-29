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

![image](https://github.com/IamZY/JVM/blob/master/images/20191228152344.png)

## 类装载器

负责加载class文件 class文件在**文件开头有特定的文件标识**，将class文件字节码内容加载到内存中，并将这些内容转换成方法去中运行时数据结构并且ClassLoader只负责class文件的加载，至于它是否可以运行，则由Execution Engine决定。

+ **文件开头有特定的文件标识**

  > cafe babe

### 虚拟机自带的加载器

![image-20191228154656126](https://github.com/IamZY/JVM/blob/master/images/image-20191228154656126.png)

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

### 双亲委派机制

当一个类收到了类加载请求，他首先不会尝试自己去加载这个类，而是把这个请求委派给父类去完成，每一个层次类加载器都是如此，因此所有的加载请求都应该传送到启动类加载其中，只有当父类加载器反馈自己无法完成这个请求的时候（在它的加载路径下没有找到所需加载的Class），子类加载器才会尝试自己去加载。

采用双亲委派的一个好处是比如加载位于 rt.jar 包中的类 java.lang.Object，不管是哪个加载器加载这个类，最终都是委托给顶层的启动类加载器进行加载，这样就保证了使用不同的类加载器最终得到的都是同样一个 Object对象。 

```java
public class String {
    public static void main(String[] args) {
        // 在类Java.lang.String 类中没有main方法
    }
}
```

### 沙箱安全

Java安全模型的核心就是Java沙箱（sandbox），什么是沙箱？沙箱是一个限制程序运行的环境。沙箱机制就是将 Java 代码限定在虚拟机(JVM)特定的运行范围中，并且严格限制代码对本地系统资源访问，通过这样的措施来保证对代码的有效隔离，防止对本地系统造成破坏。沙箱**主要限制系统资源访问**，那系统资源包括什么？——`CPU、内存、文件系统、网络`。不同级别的沙箱对这些资源访问的限制也可以不一样。

https://blog.csdn.net/qq_30336433/article/details/83268945

## 方法区

## Java栈 （普通方法）

## 本地方法栈 （Native Method Stack）

native修饰的方法

### Native Interface**本地接口**

本地接口的作用是融合不同的编程语言为 Java 所用，它的初衷是融合 C/C++程序，Java 诞生的时候是 C/C++横行的时候，要想立足，必须有调用 C/C++程序，于是就在内存中专门开辟了一块区域处理标记为native的代码，它的具体做法是 Native Method Stack中登记 native方法，在Execution Engine 执行时加载native libraies。

 目前该方法使用的越来越少了，除非是与硬件有关的应用，比如通过Java程序驱动打印机或者Java系统管理生产设备，在企业级应用中已经比较少见。因为现在的异构领域间的通信很发达，比如可以使用 Socket通信，也可以使用Web Service等等，不多做介绍。

```java
public class Test {
    public static void main(String[] args) {
        Thread t1 = new Thread();
        t1.start();
        t1.start();  // 不合法线程状态  start方法只能调用一次
    }
}


public class Thread {
    
    public synchronized void start() {
        start0();
    }
    
    // 
    // native  借助第三方实现
    public native void start0();
}

```

## 程序计数器

每个线程都有一个程序计数器，是线程私有的,就是一个指针，指向方法区中的方法字节码（用来存储指向下一条指令的地址,也即将要执行的指令代码），由执行引擎读取下一条指令，是一个非常小的内存空间，几乎可以忽略不记。

这块内存区域很小，它是当前线程所执行的字节码的行号指示器，字节码解释器通过改变这个计数器的值来选取下一条需要执行的字节码指令。

如果执行的是一个Native方法，那这个计数器是空的。

用以完成分支、循环、跳转、异常处理、线程恢复等基础功能。不会发生内存溢出(OutOfMemory=OOM)错误



























