package lab1.task3.domain.simulation;

public interface Task {
  long getDurationMs();

  void execute();

  String getDescription();
}