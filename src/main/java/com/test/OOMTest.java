package com.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试OOM生成hprof文件，用jdk自带工具jvisualvm分析
 *
 * 添加运行JVM参数:
 *
 * -Xmx100m -Xms100m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/Users/youyoukele/logs/heapdump.hprof
 */
public class OOMTest {

    public static void main(String[] args) {
        List list = new ArrayList();
        while (true) {
            list.add(new User());
        }
    }

    static class User {
        byte[] bytes = new byte[60000 * 1024];
    }

}
