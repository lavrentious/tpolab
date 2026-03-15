package tpo.lab2.log.module

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tpo.lab2.log.NaturalLogarithm

class NaturalLogarithmTest {
    private lateinit var ln: NaturalLogarithm
    private val precision = BigDecimal("0.000001")

    @BeforeEach
    fun init() {
        ln = NaturalLogarithm()
    }

    @Test
    fun shouldNotCalculateForZero() {
        assertThrows(ArithmeticException::class.java) { ln.calculate(BigDecimal.ZERO, precision) }
    }

    @Test
    fun shouldCalculateForOne() {
        assertEquals(BigDecimal.ZERO.setScale(6, HALF_EVEN), ln.calculate(BigDecimal.ONE, precision))
    }

    @ParameterizedTest(name = "ln({0})")
    @ValueSource(doubles = [0.5, 0.707, 1.2, 2.0, 2.2])
    fun testLn(d: Double) {
        val expected = BigDecimal.valueOf(kotlin.math.ln(d)).setScale(6, HALF_EVEN)
        assertEquals(expected, ln.calculate(BigDecimal.valueOf(d), precision))
    }
}
