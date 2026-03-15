package tpo.lab2.function.module

import java.math.BigDecimal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import tpo.lab2.function.AbstractFunction

class AbstractFunctionTest {
    private val function = TestFunction()
    private val x = BigDecimal.ONE

    @Test
    fun shouldThrowForZeroPrecision() {
        val exception = assertThrows(ArithmeticException::class.java) {
            function.calculate(x, BigDecimal.ZERO)
        }

        assertEquals("precision must be (0,1]", exception.message)
    }

    @Test
    fun shouldThrowForNegativePrecision() {
        val exception = assertThrows(ArithmeticException::class.java) {
            function.calculate(x, BigDecimal("-0.1"))
        }

        assertEquals("precision must be (0,1]", exception.message)
    }

    @Test
    fun shouldThrowForPrecisionGreaterThanOne() {
        val exception = assertThrows(ArithmeticException::class.java) {
            function.calculate(x, BigDecimal("1.1"))
        }

        assertEquals("precision must be (0,1]", exception.message)
    }

    private class TestFunction : AbstractFunction() {
        override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
            validateOrThrow(x, precision)
            return x
        }
    }
}
