package com.ntuzy.juc_01;

/**
 * @author IamZY
 * default
 */

@FunctionalInterface
        // 函数式接口
interface Foo {
    //    public void sayHello();
    public int add(int x, int y);

    // static方法可以多个
    public static int div(int x, int y) {
        return x / y;
    }

    // default方法可以多个
    public default int mul(int x, int y) {
        return x * y;
    }
}


public class LambdaExpressDemo02 {
    public static void main(String[] args) {
//        Foo foo = new Foo() {
//            @Override
//            public void sayHello() {
//                System.out.println("say Hello");
//            }
//        };
//
//        foo.sayHello();

        Foo foo = (x, y) -> {
            return x + y;
        };

        int sum = foo.add(1, 1);
        System.out.println(sum);

        System.out.println(foo.mul(2, 5));
    }
}
