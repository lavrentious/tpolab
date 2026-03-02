package lab1.task3.domain.personnel;

import lab1.task3.domain.command.Order;

public class Soldier extends MilitaryPersonnel {
  private final int liftingCapacityKg;

  public Soldier(String name, int liftingCapacityKg) {
    super(name, MilitaryRank.PRIVATE);
    this.liftingCapacityKg = liftingCapacityKg;
  }

  public int getLiftingCapacityKg() {
    return liftingCapacityKg;
  }

  @Override
  protected void receiveOrder(Order order) {
    System.out.println(getName() + ": Я!");
  }
}