package lab1.task3.domain.personnel;

public enum MilitaryRank {
  PRIVATE(1),
  SERGEANT(2),
  LIEUTENANT(3),
  COMMANDER(4);

  private final int level;

  MilitaryRank(int level) {
    this.level = level;
  }

  public boolean isHigherThan(MilitaryRank other) {
    return this.level > other.level;
  }
}