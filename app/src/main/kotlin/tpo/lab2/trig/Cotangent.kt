package tpo.lab2.trig

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import tpo.lab2.function.AbstractFunction

class Cotangent(
    private val sine: Sine = Sine(),
    private val cosine: Cosine = Cosine(),
) : AbstractFunction() {
    override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        validateOrThrow(x, precision)

        val intermediatePrecision = precision.setScale(precision.scale() + 5, HALF_EVEN)
        val sin = sine.calculate(x, intermediatePrecision)
        val cos = cosine.calculate(x, intermediatePrecision)

        if (sin.abs() < precision) {
            throw ArithmeticException("cot: bad x = $x")
        }

        return cos.divide(sin, precision.scale(), HALF_EVEN)
    }
}
