package com.learnreactiveprogramming.learning.functional;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionalExample {

    public static void main(String[] args) {
        var names = List.of("Alex", " Ben", "Chris", "Ben");
        var newList = namesGreaterThanSize(names, 3);
        System.out.println("Names list: " + newList);
    }

    public static List<String> namesGreaterThanSize(List<String> names, int size) {
        return names.stream()
                .filter(name -> name.length() > size)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

}
