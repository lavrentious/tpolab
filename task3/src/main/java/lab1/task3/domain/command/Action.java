package lab1.task3.domain.command;

import lab1.task3.domain.equipment.PhysicalObject;
import lab1.task3.domain.personnel.Squad;

public interface Action {
  AttemptResult execute(PhysicalObject target, Squad squad);

  String getDescription();
}