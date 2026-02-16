package lab1.task3;

import java.time.Duration;

public class EvaporationEvent {
  private final Person person;
  private final Duration timeAfterStart;
  private final ChemicalComposition composition;

  public EvaporationEvent(Person person, Duration timeAfterStart, ChemicalComposition composition) {
    if (person == null || timeAfterStart == null || composition == null) {
      throw new IllegalArgumentException("params cannot be null");
    }
    if (timeAfterStart.isNegative()) {
      throw new IllegalArgumentException("time cannot be negative");
    }
    this.person = person;
    this.timeAfterStart = timeAfterStart;
    this.composition = composition;
  }

  public Person getPerson() {
    return person;
  }

  public Duration getTimeAfterStart() {
    return timeAfterStart;
  }

  public ChemicalComposition getComposition() {
    return composition;
  }

  public void trigger() {
    person.evaporate(composition);
  }
}
