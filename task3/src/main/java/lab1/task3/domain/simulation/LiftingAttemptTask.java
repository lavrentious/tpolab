package lab1.task3.domain.simulation;

import lab1.task3.domain.command.AttemptResult;
import lab1.task3.domain.command.Order;
import lab1.task3.domain.personnel.Squad;

public class LiftingAttemptTask implements Task {
  private final Order order;
  private final Squad squad;
  private AttemptResult lastResult;

  public LiftingAttemptTask(Order order, Squad squad) {
    this.order = order;
    this.squad = squad;
  }

  @Override
  public long getDurationMs() {
    return 5000;
  }

  @Override
  public void execute() {
    this.lastResult = order.getAction().execute(order.getSubject(), squad);
  }

  @Override
  public String getDescription() {
    return String.format("squad of %d soldiers: trying to %s %s",
        squad.getSize(),
        order.getAction().getDescription(),
        order.getSubject().getName());
  }

  public AttemptResult getLastResult() {
    if (lastResult == null) {
      throw new IllegalStateException("Task has not yet finished");
    }
    return lastResult;
  }
}