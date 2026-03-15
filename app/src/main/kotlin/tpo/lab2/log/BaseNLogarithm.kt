package tpo.lab2.log

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode.HALF_EVEN
import tpo.lab2.function.AbstractFunction

class BaseNLogarithm(
    private val base: Int = 10,
    private val naturalLogarithm: NaturalLogarithm = NaturalLogarithm(),
) : AbstractFunction() {
    override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        validateOrThrow(x, precision)

        if (x <= BigDecimal.ZERO) {
            throw ArithmeticException("Logarithm base $base is undefined for x = $x")
        }

        val numerator = naturalLogarithm.calculate(x, precision)
        val denominator = naturalLogarithm.calculate(BigDecimal.valueOf(base.toLong()), precision)
        val result = numerator.divide(denominator, MathContext.DECIMAL128.precision, HALF_EVEN)

        return result.setScale(precision.scale(), HALF_EVEN)
    }
}
