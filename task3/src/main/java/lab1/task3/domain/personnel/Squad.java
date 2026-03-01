package lab1.task3.domain.personnel;

import java.util.ArrayList;
import java.util.List;

public class Squad {
  private final List<Soldier> soldiers = new ArrayList<>();

  public void addSoldier(Soldier soldier) {
    soldiers.add(soldier);
  }

  public int getTotalStrength() {
    return soldiers.stream().mapToInt(Soldier::getLiftingCapacityKg).sum();
  }

  public int getSize() {
    return soldiers.size();
  }
}