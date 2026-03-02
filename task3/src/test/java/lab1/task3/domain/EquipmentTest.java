package lab1.task3.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lab1.task3.domain.equipment.Tank;
import lab1.task3.domain.personnel.Commander;
import lab1.task3.domain.personnel.Soldier;
import lab1.task3.domain.personnel.Squad;

public class EquipmentTest {

  @Test
  void tankShouldInheritVehicleAndEquipmentProperties() {
    Tank tank = new Tank("test_inv_id", "T-34", 30000, 4);

    assertEquals("test_inv_id", tank.getInventoryId());
    assertEquals(0, tank.getWheelCount());
    assertEquals("T-34", tank.getName());
    assertEquals(30000, tank.getWeightKg());
    assertEquals(4, tank.getCapacity());
  }

  @Test
  void vehicleShouldAddIndividualCrewMember() {
    Tank tank = new Tank("test_inv_id", "T-34", 30000, 4);
    Soldier soldier = new Soldier("Ivanov", 100);

    tank.addCrewMember(soldier);

    assertEquals(1, tank.getCrew().size());
    assertTrue(tank.getCrew().contains(soldier));
  }

  @Test
  void vehicleShouldThrowExceptionWhenCapacityExceeded() {
    Tank tank = new Tank("test_inv_id", "Small-Tank", 15000, 1);
    Soldier soldier1 = new Soldier("Ivanov", 100);
    Soldier soldier2 = new Soldier("Petrov", 100);

    tank.addCrewMember(soldier1);

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> tank.addCrewMember(soldier2));
    assertTrue(exception.getMessage().contains("Crew is full"));
  }

  @Test
  void vehicleShouldLoadAndUnloadEntireSquad() {
    Tank tank = new Tank("test_inv_id", "T-34", 30000, 4);
    Squad squad = new Squad();
    Soldier ivanov = new Soldier("Ivanov", 100);
    Soldier petrov = new Soldier("Petrov", 100);
    squad.addSoldier(ivanov);
    squad.addSoldier(petrov);

    tank.addSquad(squad);
    assertEquals(2, tank.getCrew().size());
    assertEquals(1, tank.getLoadedSquads().size());

    tank.removeSquad(squad);
    assertEquals(0, tank.getCrew().size());
    assertEquals(0, tank.getLoadedSquads().size());
  }

  @Test
  void vehicleShouldRemoveEntireSquadWhenOneMemberIsRemoved() {
    Tank tank = new Tank("test_inv_id", "T-34", 30000, 4);
    Squad squad = new Squad();
    Soldier ivanov = new Soldier("Ivanov", 100);
    Soldier petrov = new Soldier("Petrov", 100);
    squad.addSoldier(ivanov);
    squad.addSoldier(petrov);

    tank.addSquad(squad);

    tank.removeCrewMember(ivanov);

    assertEquals(0, tank.getCrew().size(), "Экипаж должен быть пуст");
    assertEquals(0, tank.getLoadedSquads().size(), "Список отрядов должен быть пуст");
  }

  @Test
  void vehicleShouldNotAllowDuplicateCrewMembers() {
    Tank tank = new Tank("test_inv_id", "T-34", 30000, 4);
    Soldier soldier = new Soldier("Ivanov", 100);

    tank.addCrewMember(soldier);

    assertThrows(IllegalArgumentException.class, () -> tank.addCrewMember(soldier));
  }

  @Test
  void vehicleShouldThrowExceptionWhenSquadExceedsCapacity() {
    Tank tank = new Tank("inv-1", "T-34", 30000, 2);

    Squad squad = new Squad();
    squad.addSoldier(new Soldier("Ivanov", 100));
    squad.addSoldier(new Soldier("Petrov", 100));
    squad.addSoldier(new Soldier("Sidorov", 100));

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> tank.addSquad(squad));
    assertTrue(exception.getMessage().contains("Not enough capacity"));
  }

  @Test
  void vehicleShouldThrowExceptionWhenSquadAlreadyLoaded() {
    Tank tank = new Tank("inv-1", "T-34", 30000, 4);
    Squad squad = new Squad();
    squad.addSoldier(new Soldier("Ivanov", 100));

    tank.addSquad(squad);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> tank.addSquad(squad));
    assertEquals("Squad is already loaded", exception.getMessage());
  }

  @Test
  void vehicleShouldThrowExceptionWhenSoldierFromSquadAlreadyLoadedIndividually() {

    Tank tank = new Tank("inv-1", "T-34", 30000, 4);
    Soldier ivanov = new Soldier("Ivanov", 100);

    tank.addCrewMember(ivanov);

    Squad squad = new Squad();
    squad.addSoldier(ivanov);
    squad.addSoldier(new Soldier("Petrov", 100));

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> tank.addSquad(squad));
    assertTrue(exception.getMessage().contains("is already loaded individually"));
  }

  @Test
  void vehicleShouldThrowExceptionWhenRemovingNonExistentMember() {
    Tank tank = new Tank("inv-1", "T-34", 30000, 4);
    Soldier ghost = new Soldier("Ghost", 100);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> tank.removeCrewMember(ghost));
    assertTrue(exception.getMessage().contains("is not in the vehicle"));
  }

  @Test
  void vehicleShouldRemoveIndividualMemberWithoutAffectingSquads() {
    Tank tank = new Tank("inv-1", "T-34", 30000, 4);
    Soldier loneWolf = new Soldier("Lone Wolf", 100);

    tank.addCrewMember(loneWolf);
    tank.removeCrewMember(loneWolf);

    assertEquals(0, tank.getCrew().size(), "Танк должен быть пуст");
  }

  @Test
  void vehicleShouldProperlyRemoveNonSoldierMember() {
    Tank tank = new Tank("inv-1", "T-34", 30000, 4);
    Commander commander = new Commander("General");

    tank.addCrewMember(commander);
    tank.removeCrewMember(commander);

    assertEquals(0, tank.getCrew().size(), "Командир должен успешно выгрузиться");
  }
}