package tpo.lab2.function

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import tpo.lab2.log.BaseNLogarithm
import tpo.lab2.log.NaturalLogarithm
import tpo.lab2.trig.Cosecant
import tpo.lab2.trig.Cosine
import tpo.lab2.trig.Cotangent
import tpo.lab2.trig.Secant
import tpo.lab2.trig.Tangent

class MainFunction(
    private val cosine: Cosine = Cosine(),
    private val tangent: Tangent = Tangent(),
    private val cotangent: Cotangent = Cotangent(),
    private val secant: Secant = Secant(),
    private val cosecant: Cosecant = Cosecant(),
    private val naturalLogarithm: NaturalLogarithm = NaturalLogarithm(),
    private val log3: BaseNLogarithm = BaseNLogarithm(3),
    private val log5: BaseNLogarithm = BaseNLogarithm(5),
    private val log10: BaseNLogarithm = BaseNLogarithm(10),
) : AbstractFunction() {
    override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        validateOrThrow(x, precision)

        val internalScale = precision.scale() + 12
        val internalPrecision = precision.setScale(internalScale, HALF_EVEN)

        val result = if (x <= BigDecimal.ZERO) {
            calculateTrigBranch(x, internalPrecision)
        } else {
            calculateLogBranch(x, internalPrecision)
        }

        return result.setScale(precision.scale(), HALF_EVEN)
    }

    private fun calculateTrigBranch(x: BigDecimal, precision: BigDecimal): BigDecimal {
        val cos = cosine.calculate(x, precision)
        val sinOverCos = tangent.calculate(x, precision)
        val cosOverSin = cotangent.calculate(x, precision)
        val sec = secant.calculate(x, precision)
        val csc = cosecant.calculate(x, precision)

        val firstTerm = cos.pow(2).divide(csc.reciprocal(precision), precision.scale(), HALF_EVEN)
        val secondTerm = firstTerm.subtract(csc)
        val thirdTerm = secondTerm.add(sec.divide(cos, precision.scale(), HALF_EVEN))
        val fourthTerm = thirdTerm.multiply(cosOverSin)
        val fifthTerm = fourthTerm.subtract(
            sinOverCos.divide(cos.subtract(sinOverCos), precision.scale(), HALF_EVEN)
        )
        val sixthTerm = cos.add(cos.divide(cos, precision.scale(), HALF_EVEN)).add(cos)

        return fifthTerm.subtract(sixthTerm)
    }

    private fun calculateLogBranch(x: BigDecimal, precision: BigDecimal): BigDecimal {
        val ln = naturalLogarithm.calculate(x, precision)
        val logBase3 = log3.calculate(x, precision)
        val logBase5 = log5.calculate(x, precision)
        val logBase10 = log10.calculate(x, precision)

        val numerator = logBase3.divide(ln, precision.scale(), HALF_EVEN).pow(2).add(logBase5)
        val denominator = logBase10.divide(ln.pow(3), precision.scale(), HALF_EVEN)

        return numerator.divide(denominator, precision.scale(), HALF_EVEN).pow(3)
    }

    private fun BigDecimal.reciprocal(precision: BigDecimal): BigDecimal {
        return BigDecimal.ONE.divide(this, precision.scale(), HALF_EVEN)
    }
}
