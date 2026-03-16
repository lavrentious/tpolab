package tpo.lab2.trig

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import tpo.lab2.function.AbstractFunction

class Secant(
    private val cosine: Cosine = Cosine(),
) : AbstractFunction() {
    override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        validateOrThrow(x, precision)

        val intermediatePrecision = precision.setScale(precision.scale() + 12, HALF_EVEN)
        val cos = cosine.calculate(x, intermediatePrecision)

        if (cos.abs() < precision) {
            throw ArithmeticException("sec: bad x = $x")
        }

        return BigDecimal.ONE.divide(cos, precision.scale(), HALF_EVEN)
    }
}
