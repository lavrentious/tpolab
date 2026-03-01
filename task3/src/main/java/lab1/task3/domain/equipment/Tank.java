package lab1.task3.domain.equipment;

public class Tank extends Vehicle {
  private final int weightKg;
  private final String name;

  public Tank(String inventoryId, String name, int weightKg) {
    super(inventoryId, 0);
    this.name = name;
    this.weightKg = weightKg;
  }

  @Override
  public int getWeightKg() {
    return weightKg;
  }

  @Override
  public String getName() {
    return name;
  }
}