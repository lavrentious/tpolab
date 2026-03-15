package tpo.lab2.trig.module

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import tpo.lab2.MathConstants
import tpo.lab2.trig.Secant

class SecantTest {
    private val precision = BigDecimal("0.0000001")
    private lateinit var sec: Secant

    @BeforeEach
    fun init() {
        sec = Secant()
    }

    @Test
    fun shouldCalculateForMinimum() {
        assertEquals(BigDecimal.ONE.setScale(7, HALF_EVEN), sec.calculate(BigDecimal.ZERO, precision))
    }

    @Test
    fun shouldCalculateForMaximum() {
        val arg = MathConstants.PI.setScale(7, HALF_EVEN)
        assertAll(
            { assertEquals(BigDecimal.ONE.negate().setScale(7, HALF_EVEN), sec.calculate(arg.negate(), precision)) },
            { assertEquals(BigDecimal.ONE.negate().setScale(7, HALF_EVEN), sec.calculate(arg, precision)) },
        )
    }

    @Test
    fun shouldNotCalculateForPiHalf() {
        val arg = MathConstants.PI.divide(BigDecimal.valueOf(2), precision.scale() + 12, HALF_EVEN)
        val negativeArg = arg.negate()
        assertAll(
            {
                val exception = assertThrows(ArithmeticException::class.java) { sec.calculate(negativeArg, precision) }
                assertEquals("sec: bad x = $negativeArg", exception.message)
            },
            {
                val exception = assertThrows(ArithmeticException::class.java) { sec.calculate(arg, precision) }
                assertEquals("sec: bad x = $arg", exception.message)
            },
        )
    }

    @ParameterizedTest(name = "sec({0})")
    @CsvFileSource(resources = ["/sec.csv"], numLinesToSkip = 1)
    fun testSec(x: BigDecimal, y: BigDecimal) {
        assertEquals(y, sec.calculate(x, precision))
    }
}
