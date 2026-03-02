package lab1.task3.domain.equipment;

public abstract class MilitaryEquipment implements PhysicalObject {
  private final String inventoryId;

  protected MilitaryEquipment(String inventoryId) {
    this.inventoryId = inventoryId;
  }

  public String getInventoryId() {
    return inventoryId;
  }
}