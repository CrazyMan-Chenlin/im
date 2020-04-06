package com.roy.account;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest {
    volatile List<Integer> list = new ArrayList<>();

    @Test
    public void testLamda() {
        List<String> strings = Arrays.asList("123", "123", "456", "123");
        strings.stream().filter("123"::equals);
        Map<String, String> map = new HashMap<>();

        map.put("abc", "qwer");
        map.forEach((key, value) -> {
            if (key.equals("abc") && value.equals("qwer")) {
                System.out.println(key + "-fanggeek-" + value);
            } else {
                System.out.println("{" + key + "}" + "-" + "fanggeek" + "{" + value + "}");
            }
        });

    }


    @Test
    public void Test() throws InterruptedException {
        Map<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put("1", 1);
        list.add(1);
        list.add(2);
        int count = 0;
        for (Integer integer : list) {
            if (count == 1) {
                new ThreadTest().start();
                Thread.sleep(3000);
            }
            count++;
            System.out.println(integer);
        }
    }

    class ThreadTest extends Thread {
        @Override
        public void run() {
            list.set(1, 3);
        }
    }

}
