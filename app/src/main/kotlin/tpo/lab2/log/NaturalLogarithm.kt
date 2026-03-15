package tpo.lab2.log

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import tpo.lab2.function.AbstractFunction

class NaturalLogarithm : AbstractFunction() {
    override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        validateOrThrow(x, precision)

        if (x <= BigDecimal.ZERO) {
            throw ArithmeticException("Natural logarithm is undefined for x = $x")
        }
        if (x.compareTo(BigDecimal.ONE) == 0) {
            return BigDecimal.ZERO.setScale(precision.scale(), HALF_EVEN)
        }

        val calculationScale = precision.scale() + 2
        val z = x.subtract(BigDecimal.ONE).divide(x.add(BigDecimal.ONE), calculationScale, HALF_EVEN)
        val z2 = z.pow(2)
        var result = BigDecimal.ZERO
        var term = z
        var i = 1

        do {
            result = result.add(term.divide(BigDecimal.valueOf(i.toLong()), calculationScale, HALF_EVEN))
            term = term.multiply(z2)
            i += 2
        } while (term.abs() > precision && i < seriesLength)

        return result.multiply(BigDecimal.valueOf(2)).setScale(precision.scale(), HALF_EVEN)
    }
}
