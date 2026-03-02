package lab1.task3.domain.personnel;

import lab1.task3.domain.command.Order;
import lab1.task3.domain.exception.ChainOfCommandViolationException;

public abstract class MilitaryPersonnel extends Person {
  private final MilitaryRank rank;

  protected MilitaryPersonnel(String name, MilitaryRank rank) {
    super(name);
    this.rank = rank;
  }

  public MilitaryRank getRank() {
    return rank;
  }

  public void giveOrder(Order order, MilitaryPersonnel subordinate) {
    if (!this.rank.isHigherThan(subordinate.getRank())) {
      throw new ChainOfCommandViolationException(
          String.format("%s cannot give orders to %s", this.getName(), subordinate.getName()));
    }
    subordinate.receiveOrder(order);
  }

  protected abstract void receiveOrder(Order order);
}