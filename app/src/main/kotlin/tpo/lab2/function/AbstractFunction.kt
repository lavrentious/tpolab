package tpo.lab2.function

import java.math.BigDecimal

interface FunctionRule {
    fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal
}

abstract class AbstractFunction : FunctionRule {
    companion object {
        const val MAX_ITERATIONS = 1000
    }

    protected val seriesLength: Int = MAX_ITERATIONS

    protected fun validateOrThrow(x: BigDecimal, precision: BigDecimal) {
        if (precision <= BigDecimal.ZERO || precision > BigDecimal.ONE) {
            throw ArithmeticException("precision must be (0,1]")
        }
    }
}
