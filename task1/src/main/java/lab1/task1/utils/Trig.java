package lab1.task1.utils;

public class Trig {

  public static double tan(double x) {
    return tan(x, 20);
  }

  public static double tan(double x, int n) {
    // corner cases
    if (Double.isNaN(x) || Double.isInfinite(x)) {
      return Double.NaN;
    }
    if (x == 0.0) {
      return Math.copySign(0.0, x);
    }

    // -> (-π/2, π/2)
    x = Math.IEEEremainder(x, Math.PI);

    // reduce argument
    boolean negative = x < 0.0;
    x = Math.abs(x);
    int reductions = 0;
    while (x > Math.PI / 4.0) {
      x /= 2.0;
      reductions++;
    }

    // compute series
    double t = computeTanSeries(x, n);

    // restore original argument using double-angle formula: tan(2x) = 2*tan(x) / (1
    // - tan^2(x))
    for (int i = 0; i < reductions; i++) {
      double denom = 1.0 - t * t;
      System.out.println("denom " + denom);
      if (Math.abs(denom) < 1e-15) {
        System.out.println("small denom");
        return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
      }
      t = (2.0 * t) / denom;
    }

    return negative ? -t : t;
  }

  private static double computeTanSeries(double x, int n) {
    if (n <= 0 || x == 0.0) {
      return 0.0;
    }

    double[] c = new double[n + 1];
    c[1] = 1.0;
    for (int k = 2; k <= n; k++) {
      double sum = 0.0;
      for (int j = 1; j < k; j++) {
        sum += c[j] * c[k - j];
      }
      c[k] = sum / (2 * k - 1);
    }

    // sum series: tan(x) = Σ c_k * x^{2k-1}
    double result = 0.0;
    double xPower = x;
    for (int k = 1; k <= n; k++) {
      double term = c[k] * xPower;
      result += term;

      if (k > 5 && Math.abs(term) < Math.ulp(result) * 10.0) {
        break;
      }

      if (k < n) {
        xPower *= x * x;
      }
    }
    return result;
  }
}