package lab1.task2;

import java.util.ArrayList;
import java.util.List;

public class BubbleSort {

  public static class Result {
    public int[] sortedArray;
    public List<String> trace;

    public Result(int[] sortedArray, List<String> trace) {
      this.sortedArray = sortedArray;
      this.trace = trace;
    }
  }

  public static Result sort(int[] arr) {
    List<String> trace = new ArrayList<>();
    trace.add("T0");

    int[] a = arr.clone();
    int n = a.length;

    for (int i = 0; i < n - 1; i++) {
      trace.add("T1");
      for (int j = 0; j < n - i - 1; j++) {
        trace.add("T2");
        trace.add("T3");
        if (a[j] > a[j + 1]) {
          trace.add("T4");
          int temp = a[j];
          a[j] = a[j + 1];
          a[j + 1] = temp;
        }
      }
    }

    trace.add("T5");
    return new Result(a, trace);
  }
}
