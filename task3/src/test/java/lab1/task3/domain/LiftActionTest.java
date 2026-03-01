package lab1.task3.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import lab1.task3.domain.command.AttemptResult;
import lab1.task3.domain.command.LiftAction;
import lab1.task3.domain.equipment.Tank;
import lab1.task3.domain.personnel.Soldier;
import lab1.task3.domain.personnel.Squad;

class LiftActionTest {

  @ParameterizedTest(name = "weight={0}kg, squad strength={1}kg -> expecting {2}")
  @CsvSource({
      "46000, 100,   FAILURE", // strength < weight
      "46000, 45999, FAILURE", // 1kg short
      "46000, 46000, SUCCESS", // strength == weight
      "46000, 46001, SUCCESS", // 1kg over
      "100,   500,   SUCCESS" // weight < strength
  })
  void shouldCalculateLiftingResultCorrectly(int tankWeight, int strength, AttemptResult expected) {
    Tank testTank = new Tank("test1", "test tank", tankWeight);
    Squad testSquad = new Squad();
    testSquad.addSoldier(new Soldier("test soldier", strength));
    LiftAction action = new LiftAction();

    AttemptResult actual = action.execute(testTank, testSquad);
    assertEquals(expected, actual);
  }
}