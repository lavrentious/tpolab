package lab1.task3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Person {
  private final String description;
  private final Map<Person, Emotion> emotions = new HashMap<>();
  private boolean evaporated = false;
  private ChemicalComposition compositionAfterEvaporation;
  private final List<String> actions = new ArrayList<>();

  public Person(String description) {
    if (description == null || description.trim().isEmpty()) {
      throw new IllegalArgumentException("person description cannot be null or empty");
    }
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void feel(Person target, Emotion emotion) {
    if (target == null || emotion == null)
      return;
    emotions.put(target, emotion);
  }

  public Emotion getEmotionTowards(Person target) {
    return emotions.get(target);
  }

  public boolean hasStrongNegativeEmotionTowards(Person target) {
    Emotion emotion = emotions.get(target);
    return emotion != null && emotion.getIntensity().ordinal() >= Emotion.Intensity.STRONG.ordinal()
        && emotion.getType() == Emotion.Type.NEGATIVE;
  }

  public void evaporate(ChemicalComposition composition) {
    if (evaporated) {
      throw new IllegalStateException("Person already evaporated");
    }
    this.evaporated = true;
    this.compositionAfterEvaporation = composition;
    actions.add("evaporated");
  }

  public boolean isEvaporated() {
    return evaporated;
  }

  public ChemicalComposition getCompositionAfterEvaporation() {
    return compositionAfterEvaporation;
  }

  public void addAction(String action) {
    if (action != null && !action.trim().isEmpty()) {
      actions.add(action);
    }
  }

  @Override
  public String toString() {
    return description + (evaporated ? " [EVAPORATED]" : "");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Person person))
      return false;
    return description.equals(person.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description);
  }
}
