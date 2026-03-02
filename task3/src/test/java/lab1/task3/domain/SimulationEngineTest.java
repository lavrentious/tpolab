package lab1.task3.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lab1.task3.domain.simulation.SimulationEngine;
import lab1.task3.domain.simulation.Task;

class SimulationEngineTest {

  @Test
  void runNextShouldDoNothingIfQueueIsEmpty() {
    SimulationEngine engine = new SimulationEngine();
    assertDoesNotThrow(engine::runNext);
    assertEquals(0, engine.getCurrentTime());
  }

  @Test
  void runAllShouldExecuteMultipleTasks() {
    SimulationEngine engine = new SimulationEngine();

    Task dummyTask = new Task() {
      @Override
      public long getDurationMs() {
        return 1000;
      }

      @Override
      public void execute() {
      }

      @Override
      public String getDescription() {
        return "Dummy";
      }
    };

    engine.scheduleTask(dummyTask);
    engine.scheduleTask(dummyTask);

    engine.runAll();

    assertEquals(2000, engine.getCurrentTime());
  }
}