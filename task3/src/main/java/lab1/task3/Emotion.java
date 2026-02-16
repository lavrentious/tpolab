package lab1.task3;

public class Emotion {
  public enum Type {
    POSITIVE, NEGATIVE, NEUTRAL
  }

  public enum Intensity {
    WEAK, MODERATE, STRONG, EXTREME
  }

  private final Type type;
  private final Intensity intensity;

  public Emotion(Type type, Intensity intensity) {
    this.type = type;
    this.intensity = intensity;
  }

  public Type getType() {
    return type;
  }

  public Intensity getIntensity() {
    return intensity;
  }

  public static Emotion hatred() {
    return new Emotion(Type.NEGATIVE, Intensity.EXTREME);
  }
}
