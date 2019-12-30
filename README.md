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

### GC是什么

+ 次数上频繁收集Young区
+ 次数上较少收集Old区
+ 基本不动元空间

#### GC回收算法

![image-20191230104427349](https://github.com/IamZY/JVM/blob/master/images/image-20191230104427349.png)

JVM在进行GC时，并非每次都对上面三个内存区域一起回收的，大部分时候回收的都是指新生代。
因此GC按照回收的区域又分了两种类型，一种是普通GC（minor GC），一种是全局GC（major GC or Full GC）

***Minor GC和Full GC的区别***
　　普通GC（minor GC）：只针对新生代区域的GC,指发生在新生代的垃圾收集动作，因为大多数Java对象存活率都不高，所以Minor GC非常频繁，一般回收速度也比较快。 
　　全局GC（major GC or Full GC）：指发生在老年代的垃圾收集动作，出现了Major GC，经常会伴随至少一次的Minor GC（但并不是绝对的）。Major GC的速度一般要比Minor GC慢上10倍以上 

##### 引用计数法（了解）

​	JVM基本不采用这种方式

![image-20191230104527807](https://github.com/IamZY/JVM/blob/master/images/image-20191230104527807.png)

```java
public class T {
    public static void main(String[] args) {
        // 执行的时候存在main线程和gc线程 两个线程
        // 手动收集垃圾
        System.gc();
    }
}
```

##### 复制算法

年轻代中使用的是Minor GC，这种GC算法采用的是复制算法(Copying)

 HotSpot JVM把年轻代分为了三部分：1个Eden区和2个Survivor区（分别叫from和to）。默认比例为8:1:1,一般情况下，新创建的对象都会被分配到Eden区(一些大对象特殊处理),这些对象经过第一次Minor GC后，如果仍然存活，将会被移到Survivor区。对象在Survivor区中每熬过一次Minor GC，年龄就会增加1岁，当它的年龄增加到一定程度时，就会被移动到年老代中。因为年轻代中的对象基本都是朝生夕死的(90%以上)，所以在年轻代的垃圾回收算法使用的是复制算法，复制算法的基本思想就是将内存分为两块，每次只用其中一块，当这一块内存用完，就将还活着的对象复制到另外一块上面。

***复制算法不会产生内存碎片，但是耗空间。***

![image-20191230104640656](https://github.com/IamZY/JVM/blob/master/images/image-20191230104640656.png)

在GC开始的时候，对象只会存在于Eden区和名为“From”的Survivor区，Survivor区“To”是空的。紧接着进行GC，Eden区中所有存活的对象都会被复制到“To”，而在“From”区中，仍存活的对象会根据他们的年龄值来决定去向。年龄达到一定值(年龄阈值，可以通过-XX:MaxTenuringThreshold来设置)的对象会被移动到年老代中，没有达到阈值的对象会被复制到“To”区域。经过这次GC后，Eden区和From区已经被清空。这个时候，“From”和“To”会交换他们的角色，也就是新的“To”就是上次GC前的“From”，新的“From”就是上次GC前的“To”。不管怎样，都会保证名为To的Survivor区域是空的。Minor GC会一直重复这样的过程，直到“To”区被填满，“To”区被填满之后，会将所有对象移动到年老代中。

![image-20191230104928851](https://github.com/IamZY/JVM/blob/master/images/image-20191230104928851.png)

######  缺点

复制算法它的缺点也是相当明显的。 

+ 它浪费了一半的内存，这太要命了。 
+ 如果对象的存活率很高，我们可以极端一点，假设是100%存活，那么我们需要将所有对象都复制一遍，并将所有引用地址重置一遍。复制这一工作所花费的时间，在对象存活率达到一定程度时，将会变的不可忽视。 所以从以上描述不难看出，复制算法要想使用，最起码对象的存活率要非常低才行，而且最重要的是，我们必须要克服50%内存的浪费。

##### 标记清除

老年代一般是由标记清除或者是标记清除与标记压缩（标记整理）的混合实现

![image-20191230111357463](https://github.com/IamZY/JVM/blob/master/images/image-20191230111357463.png)

![image-20191230111410015](https://github.com/IamZY/JVM/blob/master/images/image-20191230111410015.png)

用通俗的话解释一下标记清除算法，就是当程序运行期间，若可以使用的内存被耗尽的时候，GC线程就会被触发并将程序暂停，随后将要回收的对象标记一遍，最终统一回收这些对象，完成标记清理工作接下来便让应用程序恢复运行。

主要进行两项工作，第一项则是标记，第二项则是清除。  

+ 标记：从引用根节点开始标记遍历所有的GC Roots， 先标记出要回收的对象。
+ 清除：遍历整个堆，把标记的对象清除。 
+ 缺点：此算法需要暂停整个应用，会产生内存碎片 

###### 缺点

+ 首先，它的缺点就是效率比较低（递归与全堆对象遍历），而且在进行GC的时候，需要停止应用程序，这会导致用户体验非常差劲
+ 其次，主要的缺点则是这种方式清理出来的空闲内存是不连续的，这点不难理解，我们的死亡对象都是随即的出现在内存的各个角落的，现在把它们清除之后，内存的布局自然会乱七八糟。而为了应付这一点，JVM就不得不维持一个内存的空闲列表，这又是一种开销。而且在分配数组对象的时候，寻找连续的内存空间会不太好找。 

##### 标记压缩

![image-20191230112210081](https://github.com/IamZY/JVM/blob/master/images/image-20191230112210081.png)

整理压缩阶段，不再对标记的对像做回收，而是通过所有存活对像都向一端移动，然后直接清除边界以外的内存。
可以看到，标记的存活对象将会被整理，按照内存地址依次排列，而未被标记的内存会被清理掉。如此一来，当我们需要给新对象分配内存时，JVM只需要持有一个内存的起始地址即可，这比维护一个空闲列表显然少了许多开销。 

标记/整理算法不仅可以弥补标记/清除算法当中，内存区域分散的缺点，也消除了复制算法当中，内存减半的高额代价

###### 缺点

+ 标记/整理算法唯一的缺点就是效率也不高，不仅要标记所有存活对象，还要整理所有存活对象的引用地址
+ 从效率上来说，标记/整理算法要低于复制算法。

###### 标记清除压缩(Mark-Sweep-Compact)

![image-20191230112435833](https://github.com/IamZY/JVM/blob/master/images/image-20191230112435833.png)

#### GC算法总结

+ 内存效率：复制算法>标记清除算法>标记整理算法（此处的效率只是简单的对比时间复杂度，实际情况不一定如此）。 
+ 内存整齐度：复制算法=标记整理算法>标记清除算法。 
+ 内存利用率：标记整理算法=标记清除算法>复制算法。 

可以看出，效率上来说，复制算法是当之无愧的老大，但是却浪费了太多内存，而为了尽量兼顾上面所提到的三个指标，标记/整理算法相对来说更平滑一些，但效率上依然不尽如人意，它比复制算法多了一个标记的阶段，又比标记/清除多了一个整理内存的过程

难道就没有一种最优算法吗？ 猜猜看，下面还有

> 回答：无，没有最好的算法，只有最合适的算法。==========>分代收集算法。

##### 年轻代(Young Gen)  

年轻代特点是区域相对老年代较小，对像存活率低。

这种情况复制算法的回收整理，速度是最快的。复制算法的效率只和当前存活对像大小有关，因而很适用于年轻代的回收。而复制算法内存利用率不高的问题，通过hotspot中的两个survivor的设计得到缓解。

##### 老年代(Tenure Gen)

老年代的特点是区域较大，对像存活率高。

这种情况，存在大量存活率高的对像，复制算法明显变得不合适。一般是由***标记清除或者是标记清除与标记整理的混合实现。***

+ Mark阶段的开销与存活对像的数量成正比，这点上说来，对于老年代，标记清除或者标记整理有一些不符，但可以通过多核/线程利用，对并发、并行的形式提标记效率。

+ Sweep阶段的开销与所管理区域的大小形正相关，但Sweep“就地处决”的特点，回收的过程没有对像的移动。使其相对其它有对像移动步骤的回收算法，仍然是效率最好的。但是需要解决内存碎片问题。

+ Compact阶段的开销与存活对像的数据成开比，如上一条所描述，对于大量对像的移动是很大开销的，做为老年代的第一选择并不合适。

基于上面的考虑，老年代一般是由标记清除或者是标记清除与标记整理的混合实现。以hotspot中的***CMS(Compact-Mark-Sweep)回收器***为例，CMS是基于Mark-Sweep实现的，对于对像的回收效率很高，而对于碎片问题，CMS采用基于Mark-Compact算法的Serial Old回收器做为补偿措施：当内存回收不佳（碎片导致的Concurrent Mode Failure时），将采用Serial Old执行Full GC以达到对老年代内存的整理。

#### G1 GC

https://zhuanlan.zhihu.com/p/22591838

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

# JMM

JMM(Java内存模型Java Memory Model,简称JMM)本身是一种抽象的概念 并不真实存在,它描述的是一组规则或规范通过规范定制了程序中各个变量(包括实例字段,静态字段和构成数组对象的元素)的访问方式.
JMM关于同步规定:

+ 线程解锁前,必须把共享变量的值刷新回主内存
+ 线程加锁前,必须读取主内存的最新值到自己的工作内存
+ 加锁解锁是同一把锁

由于JVM运行程序的实体是线程,而每个线程创建时JVM都会为其创建一个工作内存(有些地方成为栈空间),工作内存是每个线程的私有数据区域,而Java内存模型中规定所有变量都存储在主内存,主内存是共享内存区域,所有线程都可访问,但线程对变量的操作(读取赋值等)必须在工作内存中进行,首先要将变量从主内存拷贝到自己的工作空间,然后对变量进行操作,操作完成再将变量写回主内存,不能直接操作主内存中的变量,各个线程中的工作内存储存着主内存中的变量副本拷贝,因此不同的线程无法访问对方的工作内存,此案成间的通讯(传值) 必须通过主内存来完成,其简要访问过程如下图:

![image-20191230114624845](https://github.com/IamZY/JVM/blob/master/images/image-20191230114624845.png)

+  可见性

+  原子性

+ 有序性

```java
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

```



























