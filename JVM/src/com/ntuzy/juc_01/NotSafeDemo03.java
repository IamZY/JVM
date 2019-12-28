package com.ntuzy.juc_01;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

/**
 * 故障现象
 * java.util.ConcurrentModificationException
 * 导致原因
 * 多线程竞争
 * 解决方法
 * 1 Vector
 * 2 Collections.synchronizedList(new ArrayList<>());
 * 3 new CopyOnWriteArrayList<>();
 *
 * @Author IamZY
 * @create 2019/12/27 11:29
 */
public class NotSafeDemo03 {

    public static void main(String[] args) {
//        List<String> list = listNotSafe();
//        setNotSafe();

        // HashMap
//        Map<String, String> map = new HashMap<>();  // 不安全
        Map<String, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                map.put(UUID.randomUUID().toString().substring(0, 8), UUID.randomUUID().toString().substring(0, 8));
                System.out.println(map);
            }).start();
        }


    }

    private static void setNotSafe() {
        //        Set<String> set = new HashSet<>();   // 底层是HashMap
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }).start();
        }
    }

    private static List<String> listNotSafe() {
        //        HashMap
//        List<String> list = new ArrayList<>();
//        List<String> list = new Vector<>();
        // Collection
        // Collections
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
        List<String> list = new CopyOnWriteArrayList<>();


//        list.add("1");
//        list.add("1");
//        list.add("1");
//        list.forEach(System.out::println);

        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(list);
            }).start();
        }
        return list;
    }

}
