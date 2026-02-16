package lab1.task3;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ChemicalComposition {
  private final Map<String, Double> elements; // element -> percentage

  public ChemicalComposition(Map<String, Double> elements) {
    validateComposition(elements);
    this.elements = Map.copyOf(elements);
  }

  private void validateComposition(Map<String, Double> elements) {
    if (elements == null || elements.isEmpty()) {
      throw new IllegalArgumentException("composition cannot be null or empty");
    }
    double total = elements.values().stream().mapToDouble(Double::doubleValue).sum();
    if (Math.abs(total - 100.0) > 0.01) {
      throw new IllegalArgumentException("Sum of percentages must be 100%, got: " + total);
    }
  }

  public boolean containsElement(String element) {
    return elements.containsKey(element);
  }

  public Set<String> getElements() {
    return elements.keySet();
  }

  @Override
  public String toString() {
    return elements.entrySet().stream()
        .map(e -> String.format("%s: %.1f%%", e.getKey(), e.getValue()))
        .collect(Collectors.joining(", "));
  }
}