package tpo.lab2.trig

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import tpo.lab2.function.AbstractFunction

class Cosecant(
    private val sine: Sine = Sine(),
) : AbstractFunction() {
    override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        validateOrThrow(x, precision)

        val intermediatePrecision = precision.setScale(precision.scale() + 12, HALF_EVEN)
        val sin = sine.calculate(x, intermediatePrecision)

        if (sin.abs() < precision) {
            throw ArithmeticException("csc: bad x = $x")
        }

        return BigDecimal.ONE.divide(sin, precision.scale(), HALF_EVEN)
    }
}
