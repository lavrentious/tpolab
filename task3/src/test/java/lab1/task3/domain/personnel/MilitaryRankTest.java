package lab1.task3.domain.personnel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lab1.task3.domain.command.LiftAction;
import lab1.task3.domain.command.Order;
import lab1.task3.domain.equipment.Tank;

public class MilitaryRankTest {
  @Test
  void commanderShouldBeHigherThanOthers() {
    assertTrue(MilitaryRank.COMMANDER.isHigherThan(MilitaryRank.LIEUTENANT));
    assertTrue(MilitaryRank.COMMANDER.isHigherThan(MilitaryRank.SERGEANT));
    assertTrue(MilitaryRank.COMMANDER.isHigherThan(MilitaryRank.PRIVATE));
  }

  @Test
  void rankShouldNotBeHigherThanItself() {
    assertFalse(MilitaryRank.SERGEANT.isHigherThan(MilitaryRank.SERGEANT));
  }

  @Test
  void lowerRankShouldNotBeHigherThanSuperior() {
    assertFalse(MilitaryRank.PRIVATE.isHigherThan(MilitaryRank.COMMANDER));
  }

  @Test
  void commanderShouldReceiveOrderWithoutExceptions() {
    Commander commander = new Commander("Putin");
    Order dummyOrder = new Order(new LiftAction(), new Tank("id", "tank", 100));
    assertDoesNotThrow(() -> commander.receiveOrder(dummyOrder));
  }
}
