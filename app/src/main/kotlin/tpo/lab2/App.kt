package tpo.lab2

import java.math.BigDecimal
import tpo.lab2.function.MainFunction

class App(
    private val mainFunction: MainFunction = MainFunction(),
) {
    val greeting: String = "Lab 2 function calculator"

    fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        return mainFunction.calculate(x, precision)
    }
}

fun main() {
    val app = App()
    val precision = BigDecimal("0.000001")
    val x = BigDecimal("-1.08664")

    println(app.greeting)
    println(app.calculate(x, precision))
}
