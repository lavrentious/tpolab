package tpo.lab2.log.module

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import tpo.lab2.log.BaseNLogarithm

class BaseNLogarithmTest {
    private val precision = BigDecimal("0.0000001")
    private lateinit var defaultLog: BaseNLogarithm
    private lateinit var log3: BaseNLogarithm
    private lateinit var log5: BaseNLogarithm
    private lateinit var log10: BaseNLogarithm

    @BeforeEach
    fun init() {
        defaultLog = BaseNLogarithm()
        log3 = BaseNLogarithm(3)
        log5 = BaseNLogarithm(5)
        log10 = BaseNLogarithm(10)
    }

    @Test
    fun shouldNotCalculateForZero() {
        assertThrows(ArithmeticException::class.java) { log5.calculate(BigDecimal.ZERO, precision) }
    }

    @Test
    fun shouldThrowForNegativeX() {
        val x = BigDecimal("-1")

        val exception = assertThrows(ArithmeticException::class.java) {
            log5.calculate(x, precision)
        }

        assertEquals("log base 5: bad x = -1", exception.message)
    }

    @Test
    fun shouldCalculateForOne() {
        assertEquals(BigDecimal.ZERO.setScale(7, HALF_EVEN), log5.calculate(BigDecimal.ONE, precision))
    }

    @Test
    fun shouldUseDefaultBaseTenConstructor() {
        assertEquals(BigDecimal.ONE.setScale(7, HALF_EVEN), defaultLog.calculate(BigDecimal.TEN, precision))
    }

    @ParameterizedTest(name = "log3({0})")
    @CsvFileSource(resources = ["/log3.csv"], numLinesToSkip = 1)
    fun testLog3(x: BigDecimal, y: BigDecimal) {
        assertEquals(y, log3.calculate(x, precision))
    }

    @ParameterizedTest(name = "log5({0})")
    @CsvFileSource(resources = ["/log5.csv"], numLinesToSkip = 1)
    fun testLog5(x: BigDecimal, y: BigDecimal) {
        assertEquals(y, log5.calculate(x, precision))
    }

    @ParameterizedTest(name = "log10({0})")
    @CsvFileSource(resources = ["/log10.csv"], numLinesToSkip = 1)
    fun testLog10(x: BigDecimal, y: BigDecimal) {
        assertEquals(y, log10.calculate(x, precision))
    }
}
