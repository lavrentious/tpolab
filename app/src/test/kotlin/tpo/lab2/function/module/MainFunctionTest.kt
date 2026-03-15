package tpo.lab2.function.module

import java.math.BigDecimal
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import tpo.lab2.function.MainFunction

class MainFunctionSystemTest {
    private val precision = BigDecimal("0.0000001")
    private lateinit var mainFunction: MainFunction

    @BeforeEach
    fun setUp() {
        mainFunction = MainFunction()
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.5, 0.1, 0.9])
    fun testLogBranchEquivalenceClassInsideInterval(xVal: Double) {
        val x = BigDecimal.valueOf(xVal)
        assertDoesNotThrow {
            mainFunction.calculate(x, precision)
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [2.0, 5.0, 10.0])
    fun testLogBranchEquivalenceClassOutsideInterval(xVal: Double) {
        val x = BigDecimal.valueOf(xVal)
        assertDoesNotThrow {
            mainFunction.calculate(x, precision)
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [-0.7853981, -2.3561944, -3.9269908, -5.4977871])
    fun testTrigBranchEquivalenceClasses(xVal: Double) {
        val x = BigDecimal.valueOf(xVal)
        assertDoesNotThrow {
            mainFunction.calculate(x, precision)
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = [0.0, -1.5707963, -3.1415926, -4.7123889])
    fun testTrigBranchSingularities(xVal: Double) {
        val x = BigDecimal.valueOf(xVal)
        assertThrows(ArithmeticException::class.java) {
            mainFunction.calculate(x, precision)
        }
    }

    @Test
    fun testLogBranchSingularityAtOne() {
        val x = BigDecimal.ONE
        assertThrows(ArithmeticException::class.java) {
            mainFunction.calculate(x, precision)
        }
    }

    @Test
    fun testTrigBranchPeriodicity() {
        val x1 = BigDecimal.valueOf(-0.7853981)
        val x2 = x1.subtract(BigDecimal.valueOf(2 * Math.PI))

        val result1 = mainFunction.calculate(x1, precision)
        val result2 = mainFunction.calculate(x2, precision)

        val difference = result1.subtract(result2).abs()
        assertTrue(difference < BigDecimal("0.001"))
    }
}