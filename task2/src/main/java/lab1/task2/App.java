package lab1.task2;

public class App {

  public static void printArray(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
      System.out.print(arr[i] + " ");
    }
    System.out.println();
  }

  public static void main(String[] args) {
    int[] arr = { 64, 34, 25, 12, 22, 11, 90, 5 };
    System.out.println("Unsorted array:");
    printArray(arr);
    BubbleSort.sort(arr);
    System.out.println("Sorted array:");
    printArray(arr);
  }
}
