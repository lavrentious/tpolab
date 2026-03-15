package tpo.lab2.trig

import tpo.lab2.MathConstants
import java.math.BigDecimal
import tpo.lab2.function.AbstractFunction
import java.math.MathContext
import java.math.RoundingMode
import java.math.RoundingMode.HALF_EVEN;

class Cosine : AbstractFunction {
    private val sine: Sine;

    constructor() : super() {
        this.sine = Sine()
    }

    constructor(sine: Sine) : super() {
        this.sine = sine
    }


    override fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        validateOrThrow(x, precision)
        val mc = MathContext(precision.scale() + 2, HALF_EVEN);
        val piHalf = MathConstants.PI.divide(BigDecimal(2), mc)
        return sine.calculate(piHalf.subtract(x), precision);
    }
}
