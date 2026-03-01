package lab1.task3.domain.personnel;

import lab1.task3.domain.command.Order;

public class Commander extends MilitaryPersonnel {
  public Commander(String name) {
    super(name, MilitaryRank.COMMANDER);
  }

  @Override
  protected void receiveOrder(Order order) {
  }
}