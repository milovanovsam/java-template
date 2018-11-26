package edu.spbu.sort;

import java.util.Arrays;
import java.util.List;

/**
 * Created by artemaliev on 07/09/15.
 */
public class IntSort {
  public static void sort (int array[], int low, int high) {
    if (low >= high)
      return;
    int currentLow = low;
    int currentHigh = high;
    int middle = array[low+(high-low)/2];
    while (currentLow <= currentHigh) {
      while (array[currentLow] < middle)
        currentLow++;
      while (array[currentHigh] > middle)
        currentHigh--;
      if (currentLow <= currentHigh) {
        int tmp = array[currentLow];
        array[currentLow] = array[currentHigh];
        array[currentHigh] = tmp;
        currentLow++;
        currentHigh--;
      }
    }
    if (low < currentHigh)
      sort(array, low, currentHigh);
    if (currentLow < high)
      sort(array, currentLow, high);
  }

/*  public static void sort (List<Integer> list) {
    Collections.sort(list);
  }*/
}