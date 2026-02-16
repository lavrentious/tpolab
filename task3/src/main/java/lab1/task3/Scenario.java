package lab1.task3;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Scenario {
  private final Location location;
  private final List<EvaporationEvent> scheduledEvents = new ArrayList<>();
  private Duration currentTime = Duration.ZERO;

  public Scenario(Location location) {
    this.location = location;
  }

  public void scheduleEvaporation(EvaporationEvent event) {
    scheduledEvents.add(event);
  }

  public void advanceTime(Duration duration) {
    if (duration.isNegative()) {
      throw new IllegalArgumentException("time cannot go backwards");
    }
    currentTime = currentTime.plus(duration);

    scheduledEvents.stream()
        .filter(event -> event.getTimeAfterStart().equals(currentTime))
        .forEach(EvaporationEvent::trigger);
  }

  public Duration getCurrentTime() {
    return currentTime;
  }

  public Location getLocation() {
    return location;
  }
}