package lab1.task3.domain.equipment;

public abstract class Vehicle extends MilitaryEquipment {
  private final int wheelCount;

  protected Vehicle(String inventoryId, int wheelCount) {
    super(inventoryId);
    this.wheelCount = wheelCount;
  }

  public int getWheelCount() {
    return wheelCount;
  }
}
