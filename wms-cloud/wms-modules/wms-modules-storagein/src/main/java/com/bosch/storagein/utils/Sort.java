package com.bosch.storagein.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: UWH4SZH
 * @since: 10/17/2022 10:06
 * @description:
 */
public class Sort {

    public static void main(String[] args) {
        List<String> list= new ArrayList<>();
        list.add("WA.01.001.01");
        list.add("WA.02.001.02");
        list.add("WA.03.002.02");
        list.add("WA.02.002.02");
        list.add("WA.01.002.01");


        Collections.sort(list);
        System.out.println(list);
    }
}
