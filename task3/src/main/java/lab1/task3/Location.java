package lab1.task3;

import java.util.HashSet;
import java.util.Set;

public class Location {
  private final String name;
  private final Set<Person> occupants = new HashSet<>();

  public Location(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("location name cannot be null or empty");
    }
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public boolean enter(Person person) {
    if (person == null)
      return false;
    return occupants.add(person);
  }

  public boolean leave(Person person) {
    return occupants.remove(person);
  }

  public Set<Person> getOccupants() {
    return Set.copyOf(occupants);
  }
}
