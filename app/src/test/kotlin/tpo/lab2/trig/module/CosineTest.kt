package tpo.lab2.trig.module

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import tpo.lab2.MathConstants
import tpo.lab2.trig.Cosine
import tpo.lab2.trig.Sine

class CosineTest {
    private val precision = BigDecimal("0.0000001")
    private lateinit var cos: Cosine

    @BeforeEach
    fun init() {
        cos = Cosine(Sine())
    }

    @Test
    fun shouldCalculateForZero() {
        assertEquals(BigDecimal.ONE.setScale(7, HALF_EVEN), cos.calculate(BigDecimal.ZERO, precision))
    }

    @Test
    fun shouldCalculateForPiHalf() {
        val arg = MathConstants.PI.divide(BigDecimal.valueOf(2), precision.scale() + 12, HALF_EVEN)
        val expected = BigDecimal.ZERO.setScale(7, HALF_EVEN)
        assertAll(
            { assertEquals(expected, cos.calculate(arg, precision)) },
            { assertEquals(expected, cos.calculate(arg.negate(), precision)) },
            { assertEquals(expected, cos.calculate(arg.multiply(BigDecimal.valueOf(3)), precision)) },
            { assertEquals(expected, cos.calculate(arg.multiply(BigDecimal.valueOf(3)).negate(), precision)) },
        )
    }

    @Test
    fun shouldCalculateForPi() {
        val arg = MathConstants.PI
        val expected = BigDecimal.ONE.negate().setScale(7, HALF_EVEN)
        assertAll(
            { assertEquals(expected, cos.calculate(arg, precision)) },
            { assertEquals(expected, cos.calculate(arg.negate(), precision)) },
        )
    }

    @ParameterizedTest(name = "cos({0})")
    @CsvFileSource(resources = ["/cos.csv"], numLinesToSkip = 1)
    fun testCos(x: BigDecimal, y: BigDecimal) {
        assertEquals(y, cos.calculate(x, precision))
    }
}
