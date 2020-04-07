package com.test;

import java.util.ArrayList;
import java.util.List;

public class HeapTest {

    public static void main(String[] args) throws InterruptedException {
        List list = new ArrayList();
        while (true) {
            list.add(new Obj());
            Thread.sleep(30);
        }
    }

    static class Obj {
        byte[] bytes = new byte[1024 * 100]; // 100kb
    }

}
