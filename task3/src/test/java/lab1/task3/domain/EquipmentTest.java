package lab1.task3.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lab1.task3.domain.equipment.Tank;

public class EquipmentTest {
  @Test
  void tankShouldInheritVehicleAndEquipmentProperties() {
    Tank tank = new Tank("test_inv_id", "T-34", 30000);

    assertEquals("test_inv_id", tank.getInventoryId());
    assertEquals(0, tank.getWheelCount());
    assertEquals("T-34", tank.getName());
    assertEquals(30000, tank.getWeightKg());
  }
}
