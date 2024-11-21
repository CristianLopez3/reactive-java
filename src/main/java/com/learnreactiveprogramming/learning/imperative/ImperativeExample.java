package com.learnreactiveprogramming.learning.imperative;

import java.util.ArrayList;
import java.util.List;

public class ImperativeExample {

    public static void main(String[] args) {
        var names = List.of("Alex", " Ben", "Chris");
        namesGreaterThanSize(names, 1);
    }

    public static List<String> namesGreaterThanSize(List<String> names, int size) {
        var newList = new ArrayList<String>();
        names.forEach(name ->{
            if(name.length() > size) {
                newList.add(name);
            }
        });
        return newList;
    }

}
