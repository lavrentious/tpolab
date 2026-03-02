package lab1.task3.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import lab1.task3.domain.command.LiftAction;
import lab1.task3.domain.command.Order;
import lab1.task3.domain.equipment.Tank;
import lab1.task3.domain.personnel.Squad;
import lab1.task3.domain.simulation.LiftingAttemptTask;

class LiftingAttemptTaskTest {

  @Test
  void shouldThrowExceptionIfResultRequestedBeforeExecution() {
    Order order = new Order(new LiftAction(), new Tank("id", "tank", 100, 4));
    LiftingAttemptTask task = new LiftingAttemptTask(order, new Squad());

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        task::getLastResult);
    assertEquals("Task has not yet finished", exception.getMessage());
  }
}