package com.test;

public class StackOverflowTest {

    private static int i = 0;

    public static void main(String[] args) {
        try {
            redo();
        } catch (Error e) {
            e.printStackTrace();
            System.out.println(i);
        }
    }

    private static void redo() {
        i++;
        redo();
    }

}
