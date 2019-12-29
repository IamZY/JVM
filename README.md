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

供各线程共享的运行时内存区域。它***存储了每一个类的结构信息***，例如运行时常量池（Runtime Constant Pool）、字段和方法数据、构造函数和普通方法的**字节码内容**。

上面讲的是规范，在不同虚拟机里头实现是不一样的，最典型的就是***永久代(PermGen space)和元空间(Metaspace)。***

> 方法区 f = new 永久代
>
> 方法区 f = new 元空间

***但是实例变量存在堆内存中和方法区无关***

## 堆

一个JVM实例只存在一个堆内存，堆内存的大小是可以调节的。类加载器读取了类文件后，需要把类、方法、常变量放到堆内存中，保存所有引用类型的真实信息，以方便执行器执行，堆内存逻辑上分为三部分：（物理上分为新生代+老年代）

+ 新生代 Young/New

  + 伊甸园  GC(垃圾回收)
  + 幸存者0区   From区
  + 幸存者1区  To区

+ 老年代 Old/Tenure

  养老区满了，开启Full GC。Full GC没办法清空后 OutOfMemoryError

+ 元空间

![image-20191229151435016](https://github.com/IamZY/JVM/blob/master/images/image-20191229151435016.png)

新生区是类的诞生、成长、消亡的区域，一个类在这里产生，应用，最后被垃圾回收器收集，结束生命。新生区又分为两部分： 伊甸区（Eden space）和幸存者区（Survivor pace） ，所有的类都是在伊甸区被new出来的。幸存区有两个： 0区（Survivor 0 space）和1区（Survivor 1 space）。当伊甸园的空间用完时，程序又需要创建对象，JVM的垃圾回收器将对伊甸园区进行垃圾回收(Minor GC)，将伊甸园区中的不再被其他对象所引用的对象进行销毁。然后将伊甸园中的剩余对象移动到幸存 0区。若幸存 0区也满了，再对该区进行垃圾回收，然后移动到 1 区。那如果1 区也满了呢？再移动到养老区。若养老区也满了，那么这个时候将产生MajorGC（FullGC），进行养老区的内存清理。若养老区执行了Full GC之后发现依然无法进行对象的保存，就会产生OOM异常“OutOfMemoryError”。

### **MinorGC**的过程（复制->**清空**->互换）

 ![image-20191229155101707](https://github.com/IamZY/JVM/blob/master/images/image-20191229155101707.png)

新生代里面（伊甸园、幸存者0区、幸存者1区） = 默认8：1：1

新生代：老年代=1：2

+ eden、SurvivorFrom 复制到 SurvivorTo，年龄+1 

  首先，当Eden区满的时候会触发第一次GC,把还活着的对象拷贝到SurvivorFrom区，当Eden区再次触发GC的时候会扫描Eden区和From区域,对这两个区域进行垃圾回收，经过这次回收后还存活的对象,则直接复制到To区域（如果有对象的年龄已经达到了老年的标准，则赋值到老年代区），同时把这些对象的年龄+1 

+ 清空 eden、SurvivorFrom 

  然后，清空Eden和SurvivorFrom中的对象，也即复制之后有交换，谁空谁是to

+ SurvivorTo和 SurvivorFrom 互换 

  最后，SurvivorTo和SurvivorFrom互换，原SurvivorTo成为下一次GC时的SurvivorFrom区。部分对象会在From和To区域中复制来复制去,如此交换***15***次(由JVM参数MaxTenuringThreshold决定,这个参数默认是15),最终如果还是存活,就存入到老年代

### HotSpot管理

![image-20191229160321501](https://github.com/IamZY/JVM/blob/master/images/image-20191229160321501.png)

实际而言，方法区（Method Area）和堆一样，是各个线程共享的内存区域，它用于存储虚拟机加载的：类信息+普通常量+静态常量+编译器编译后的代码等等，虽然JVM规范将方法区描述为堆的一个逻辑部分，但它却还有一个别名叫做Non-Heap(非堆)，目的就是要和堆分开。

 对于HotSpot虚拟机，很多开发者习惯将方法区称之为“永久代(Parmanent Gen)” ，但严格本质上说两者不同，或者说使用永久代来实现方法区而已，永久代是方法区(相当于是一个接口interface)的一个实现，jdk1.7的版本中，已经将原本放在永久代的字符串常量池移走。

![image-20191229160720129](https://github.com/IamZY/JVM/blob/master/images/image-20191229160720129.png)

### 永久区(**java7**之前有)

 永久存储区是一个常驻内存区域，用于存放JDK自身所携带的 Class,Interface 的元数据，也就是说它存储的是运行环境必须的类信息，被装载进此区域的数据是不会被垃圾回收器回收掉的，关闭 JVM 才会释放此区域所占用的内存。

### 堆参数调优

#### jdk1.7

![image-20191229161312189](https://github.com/IamZY/JVM/blob/master/images/image-20191229161312189.png)

#### jdk1.8

![image-20191229161427996](https://github.com/IamZY/JVM/blob/master/images/image-20191229161427996.png)

在Java8中，永久代已经被移除，被一个称为**元空间**的区域所取代。元空间的本质和永久代类似。

+ 元空间与永久代之间最大的区别在于：

  永久代使用的JVM的堆内存，但是java8以后的**元空间并不在虚拟机中而是使用本机物理内存**。

  因此，默认情况下，元空间的大小仅受本地内存限制。类的元数据放入 native memory, 字符串池和类的静态变量放入 java 堆中，这样可以加载多少类的元数据就不再由MaxPermSize 控制, 而由系统的实际可用空间来控制。

![image-20191229161736062](https://github.com/IamZY/JVM/blob/master/images/image-20191229161736062.png)

```java
public class T2 {

    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());  // 处理器

        long maxMemory = Runtime.getRuntime().maxMemory();//返回 Java 虚拟机试图使用的最大内存量（1/4）。
        long totalMemory = Runtime.getRuntime().totalMemory();//返回 Java 虚拟机中的内存总量（1/64）。
        System.out.println("-Xmx:MAX_MEMORY = " + maxMemory + "（字节）、" + (maxMemory / (double) 1024 / 1024) + "MB");  // 堆内存最大
        System.out.println("-Xms:TOTAL_MEMORY = " + totalMemory + "（字节）、" + (totalMemory / (double) 1024 / 1024) + "MB");  // 堆内存最初大小
    }

}
```

> -Xms1024m -Xmx1024m -XX:+PrintGCDetails

![image-20191229163923864](https://github.com/IamZY/JVM/blob/master/images/image-20191229163923864.png)

### 详细GC收集日志

> [GC (Allocation Failure) 
>
> [PSYoungGen: 1536K->504K(2048K)] 1536K->728K(7680K), 0.0009730 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 

![image-20191229165109224](https://github.com/IamZY/JVM/blob/master/images/image-20191229165109224.png)

>[Full GC (Ergonomics)
>
>[PSYoungGen: 1446K->0K(2048K)]
>
>[ParOldGen: 5054K->3050K(5632K)] 6500K->3050K(7680K),
>
>[Metaspace: 3179K->3179K(1056768K)], 0.0040745 secs]
>
>[Times: user=0.00 sys=0.00, real=0.00 secs] 



## Java栈 （普通方法）

栈也叫栈内存，主管Java程序的运行，是在线程创建时创建，它的生命期是跟随线程的生命期，线程结束栈内存也就释放，**对于栈来说不存在垃圾回收问题**，只要线程一结束该栈就Over，生命周期和线程一致，是线程私有的。

***8种基本类型的变量+对象的引用变量+实例方法都是在函数的栈内存中分配。***

### 存储内容

栈帧(Java 方法)中主要保存3 类数据：

+ 本地变量（Local Variables）:输入参数和输出参数以及方法内的变量；

+ 栈操作（Operand Stack）:记录出栈、入栈的操作；

+ 栈帧数据（Frame Data）:包括类文件、方法等等。

每个方法执行的同时都会创建一个栈帧，用于存储局部变量表、操作数栈、动态链接、方法出口等信息，每一个方法从调用直至执行完毕的过程，就对应着一个栈帧在虚拟机中入栈到出栈的过程。栈的大小和具体JVM的实现有关，通常在256K~756K之间,与等于1Mb左右。

```java
package com.ntuzy.juc_01;

/**
 * @Author IamZY
 * @create 2019/12/29 11:27
 */
public class JVMNote {

    // java.lang.StackOverflowError  -> 不是异常是错误
    public static void m1 () {
        m1();
    }

    public static void main(String[] args){
        m1();
    }

}
```

![image-20191229113450026](https://github.com/IamZY/JVM/blob/master/images/image-20191229113450026.png)

HotSpot是使用指针的方式来访问对象：

Java堆中会存放访问**类元数据**的地址，

reference存储的就直接是对象的地址

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



























