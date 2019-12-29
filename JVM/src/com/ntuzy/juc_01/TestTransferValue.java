package com.ntuzy.juc_01;

import sun.awt.image.IntegerInterleavedRaster;

/**
 * @Author IamZY
 * @create 2019/12/29 15:29
 */
public class TestTransferValue {

    public void changeValue1(int age) {
        age = 30;
    }

    public void changeValue2(Person person) {
        person.setPersonName("xxx");
    }

    public void changeValue3(String str) {
        str = "xxx";
    }


    public static void main(String[] args) {
        TestTransferValue test = new TestTransferValue();
        int age = 20;
        test.changeValue1(age);
        System.out.println("age---------" + age);  // 20

        Person p = new Person("abc");
        test.changeValue2(p);
        System.out.println("personName----------" + p.getPersonName());  // xxx

        String str = "abc";
        test.changeValue3(str);
        System.out.println("string-------" + str);  // abc 字符常量池

    }

    static class Person {
        String personName;

        public String getPersonName() {
            return personName;
        }

        public void setPersonName(String personName) {
            this.personName = personName;
        }

        public Person(String personName) {
            this.personName = personName;
        }

    }
}
