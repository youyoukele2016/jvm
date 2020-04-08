package com.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试gc内存流转过程，观察从Eden到Survivor再到Old的整个过程
 *
 * 用jdk自带工具jvisualvm分析(Visual GC)
 */
public class HeapMoveProcessTest {

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
