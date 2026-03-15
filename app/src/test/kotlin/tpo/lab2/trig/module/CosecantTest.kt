package tpo.lab2.trig.module

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.junit.jupiter.params.provider.ValueSource
import tpo.lab2.MathConstants
import tpo.lab2.trig.Cosecant

class CosecantTest {
    private val precision = BigDecimal("0.0000001")
    private lateinit var csc: Cosecant

    @BeforeEach
    fun init() {
        csc = Cosecant()
    }

    @Test
    fun shouldCalculateForMinimum() {
        val arg = MathConstants.PI.divide(BigDecimal.valueOf(2), precision.scale() + 12, HALF_EVEN)
        assertEquals(BigDecimal.ONE.setScale(precision.scale(), HALF_EVEN), csc.calculate(arg, precision))
    }

    @Test
    fun shouldCalculateForMaximum() {
        val arg = MathConstants.PI.divide(BigDecimal.valueOf(2), precision.scale() + 12, HALF_EVEN).negate()
        assertEquals(BigDecimal.ONE.negate().setScale(precision.scale(), HALF_EVEN), csc.calculate(arg, precision))
    }

    @ParameterizedTest
    @ValueSource(doubles = [-Math.PI, 0.0, Math.PI])
    fun shouldNotCalculateForPi(x: Double) {
        val arg = BigDecimal.valueOf(x).setScale(precision.scale(), HALF_EVEN)
        val exception = assertThrows(ArithmeticException::class.java) { csc.calculate(arg, precision) }
        assertEquals("csc: bad x = $arg", exception.message)
    }

    @ParameterizedTest(name = "csc({0})")
    @CsvFileSource(resources = ["/csc.csv"], numLinesToSkip = 1)
    fun testCsc(x: BigDecimal, y: BigDecimal) {
        assertEquals(y, csc.calculate(x, precision))
    }
}
