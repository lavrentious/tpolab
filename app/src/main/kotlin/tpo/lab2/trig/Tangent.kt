package tpo.lab2.trig

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import tpo.lab2.function.AbstractFunction

class Tangent(
    private val sine: Sine = Sine(),
    private val cosine: Cosine = Cosine(),
) : AbstractFunction() {
    override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        validateOrThrow(x, precision)

        val intermediatePrecision = precision.setScale(precision.scale() + 5, HALF_EVEN)
        val sin = sine.calculate(x, intermediatePrecision)
        val cos = cosine.calculate(x, intermediatePrecision)

        if (cos.abs() < precision) {
            throw ArithmeticException("Tangent is undefined for x = $x")
        }

        return sin.divide(cos, precision.scale(), HALF_EVEN)
    }
}
