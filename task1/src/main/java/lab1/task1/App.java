package lab1.task1;

import lab1.task1.utils.Trig;

public class App {
  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: java App <x> [<n>]");
      return;
    }

    try {
      double x = Double.parseDouble(args[0]);
      int n = args.length >= 2 ? Integer.parseInt(args[1]) : Integer.MAX_VALUE;

      double result = Trig.tan(x, n);
      if (Double.isNaN(result)) {
        System.out.println("Error: x must be in [-PI/2...PI/2].");
        return;
      }

      System.out.printf("tg(%.6f) = %.6f%n (with %d terms)", x, result, n);
    } catch (NumberFormatException e) {
      System.out.println("invalid input: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }
}
