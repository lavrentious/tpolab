package lab1.task3.domain.command;

import lab1.task3.domain.equipment.PhysicalObject;
import lab1.task3.domain.personnel.Squad;

public class LiftAction implements Action {
  @Override
  public AttemptResult execute(PhysicalObject target, Squad squad) {
    return squad.getTotalStrength() >= target.getWeightKg()
        ? AttemptResult.SUCCESS
        : AttemptResult.FAILURE;
  }

  @Override
  public String getDescription() {
    return "lift object";
  }
}