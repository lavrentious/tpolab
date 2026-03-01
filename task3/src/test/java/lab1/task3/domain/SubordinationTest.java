package lab1.task3.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lab1.task3.domain.command.LiftAction;
import lab1.task3.domain.command.Order;
import lab1.task3.domain.equipment.Tank;
import lab1.task3.domain.exception.ChainOfCommandViolationException;
import lab1.task3.domain.personnel.Commander;
import lab1.task3.domain.personnel.Soldier;

class SubordinationTest {

  private Commander commander;
  private Soldier privateIvanov;
  private Soldier privatePetrov;
  private Order dummyOrder;

  @BeforeEach
  void setUp() {
    commander = new Commander("Командир");
    privateIvanov = new Soldier("Иванов", 100);
    privatePetrov = new Soldier("Петров", 100);
    dummyOrder = new Order(new LiftAction(), new Tank("T-1", "Танк", 1000));
  }

  @Test
  void higherRankShouldGiveOrderToLowerRankSuccessfully() {
    assertDoesNotThrow(() -> commander.giveOrder(dummyOrder, privateIvanov));
  }

  @Test
  void lowerRankCannotGiveOrderToHigherRank() {
    ChainOfCommandViolationException exception = assertThrows(
        ChainOfCommandViolationException.class,
        () -> privateIvanov.giveOrder(dummyOrder, commander));

    assertTrue(exception.getMessage().contains("cannot give orders"));
  }

  @Test
  void equalRanksCannotGiveOrdersToEachOther() {
    assertThrows(
        ChainOfCommandViolationException.class,
        () -> privateIvanov.giveOrder(dummyOrder, privatePetrov));
  }
}