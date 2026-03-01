package lab1.task3.domain.simulation;

import java.util.LinkedList;
import java.util.Queue;

public class SimulationEngine {
  private long currentTime = 0;
  private final Queue<Task> taskQueue = new LinkedList<>();

  public void scheduleTask(Task task) {
    taskQueue.add(task);
  }

  public void runNext() {
    if (!taskQueue.isEmpty()) {
      Task task = taskQueue.poll();
      System.out.println(String.format("[%dms] starting: %s...", currentTime, task.getDescription()));
      currentTime += task.getDurationMs();
      task.execute();
      System.out.println(String.format("[%dms] finished.", currentTime));
    }
  }

  public void runAll() {
    while (!taskQueue.isEmpty()) {
      runNext();
    }
  }

  public long getCurrentTime() {
    return currentTime;
  }
}