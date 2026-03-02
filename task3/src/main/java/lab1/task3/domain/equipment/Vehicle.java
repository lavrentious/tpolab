package lab1.task3.domain.equipment;

import java.util.ArrayList;
import java.util.List;

import lab1.task3.domain.personnel.MilitaryPersonnel;
import lab1.task3.domain.personnel.Soldier;
import lab1.task3.domain.personnel.Squad;

public abstract class Vehicle extends MilitaryEquipment {
  private final int wheelCount;
  private final int capacity;

  private final List<MilitaryPersonnel> crew = new ArrayList<>();
  private final List<Squad> loadedSquads = new ArrayList<>();

  protected Vehicle(String inventoryId, int wheelCount, int capacity) {
    super(inventoryId);
    this.wheelCount = wheelCount;
    this.capacity = capacity;
  }

  public int getWheelCount() {
    return wheelCount;
  }

  public int getCapacity() {
    return capacity;
  }

  public List<MilitaryPersonnel> getCrew() {
    return new ArrayList<>(crew);
  }

  public List<Squad> getLoadedSquads() {
    return new ArrayList<>(loadedSquads);
  }

  public void addCrewMember(MilitaryPersonnel member) {
    if (this.crew.size() >= this.capacity) {
      throw new IllegalStateException("Crew is full. Capacity: " + capacity);
    }
    if (this.crew.contains(member)) {
      throw new IllegalArgumentException(member.getName() + " is already in the vehicle");
    }
    this.crew.add(member);
  }

  public void addSquad(Squad squad) {
    if (this.crew.size() + squad.getSize() > this.capacity) {
      throw new IllegalStateException("Not enough capacity for the squad. Needed: " + squad.getSize());
    }
    if (this.loadedSquads.contains(squad)) {
      throw new IllegalArgumentException("Squad is already loaded");
    }

    for (Soldier soldier : squad.getSoldiers()) {
      if (this.crew.contains(soldier)) {
        throw new IllegalArgumentException("Soldier " + soldier.getName() + " is already loaded individually");
      }
    }

    this.crew.addAll(squad.getSoldiers());
    this.loadedSquads.add(squad);
  }

  public void removeSquad(Squad squad) {
    if (this.loadedSquads.remove(squad)) {
      this.crew.removeAll(squad.getSoldiers());
    }
  }

  public void removeCrewMember(MilitaryPersonnel member) {
    if (!this.crew.contains(member)) {
      throw new IllegalArgumentException(member.getName() + " is not in the vehicle");
    }

    Squad associatedSquad = null;
    if (member instanceof Soldier soldier) {
      for (Squad squad : loadedSquads) {
        if (squad.getSoldiers().contains(soldier)) {
          associatedSquad = squad;
          break;
        }
      }
    }

    if (associatedSquad != null) {
      removeSquad(associatedSquad);
    } else {
      this.crew.remove(member);
    }
  }
}