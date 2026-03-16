package tpo.lab2.trig

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import tpo.lab2.function.AbstractFunction

class Sine : AbstractFunction() {
    override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        validateOrThrow(x, precision)

        val mc = MathContext(precision.scale() + 10, RoundingMode.HALF_EVEN)

        val pi = BigDecimal("3.1415926535897932384626433832795028841971").round(mc)
        val tau = pi * BigDecimal(2)

        var normalizedX = x.remainder(tau, mc)

        if (normalizedX > pi) {
            normalizedX -= tau
        } else if (normalizedX < -pi) {
            normalizedX += tau
        }

        var result = normalizedX
        var term = normalizedX
        val x2 = normalizedX.multiply(normalizedX, mc)

        var i = 1
        do {
            val denominator = BigDecimal((2L * i) * (2L * i + 1))
            term = (term.multiply(x2, mc)).divide(denominator, mc)

            result += term.multiply(minusOnePower(i), mc)
            i++

            if (i > MAX_ITERATIONS) break
        } while (term.abs() > (precision / BigDecimal.TEN))

        return result.setScale(precision.scale(), RoundingMode.HALF_EVEN)
    }

    private fun minusOnePower(n: Int): BigDecimal {
        return BigDecimal.valueOf(1L - (n % 2) * 2)
    }
}
