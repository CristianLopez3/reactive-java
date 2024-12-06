package com.learnreactiveprogramming.learning;

import java.util.ArrayList;
import java.util.List;

public class BubbleSortExample {

    private static List<Integer> bubbleSort() {
//        List<Integer> values = new ArrayList<>(List.of(5, 3, 1, 4, 2));
        List<Integer> values = new ArrayList<>(List.of(1, 2, 5, 3, 2));
        int sizeOfList = values.size();

        // Realizamos el Bubble Sort
        for (int i = 0; i < sizeOfList - 1; i++) {
            boolean swapped = false; // Bandera para verificar si hubo un intercambio
            for (int j = 0; j < sizeOfList - 1 - i; j++) {
                if (values.get(j) > values.get(j + 1)) {
                    // Intercambiar valores
                    int temp = values.get(j);
                    values.set(j, values.get(j + 1));
                    values.set(j + 1, temp);
                    swapped = true; // Se realizó un intercambio
                }
            }
            if (!swapped) {
                break;
            }
        }

        return values; // Devuelve la lista ordenada
    }

    public static void main(String[] args) {
        List<Integer> sortedValues = bubbleSort();
        System.out.println(sortedValues); // Imprimir la lista ordenada
    }
}