package lab1.task3.domain.command;

import lab1.task3.domain.equipment.PhysicalObject;

public class Order {
  private final Action action;
  private final PhysicalObject subject;

  public Order(Action action, PhysicalObject subject) {
    this.action = action;
    this.subject = subject;
  }

  public PhysicalObject getSubject() {
    return subject;
  }

  public Action getAction() {
    return action;
  }
}