package tpo.lab2

import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import tpo.lab2.function.AbstractFunction
import tpo.lab2.function.MainFunction
import tpo.lab2.log.BaseNLogarithm
import tpo.lab2.log.NaturalLogarithm
import tpo.lab2.trig.Cosecant
import tpo.lab2.trig.Cosine
import tpo.lab2.trig.Cotangent
import tpo.lab2.trig.Secant
import tpo.lab2.trig.Sine
import tpo.lab2.trig.Tangent
import tpo.lab2.util.CSVGraphWriter

class App(
    outputDir: String = DEFAULT_OUTPUT_DIR,
    private val mainFunction: MainFunction = MainFunction(),
) {
    private var outputDir: String = normalizeOutputDir(outputDir)

    fun calculate(x: BigDecimal, precision: BigDecimal): BigDecimal {
        return mainFunction.calculate(x, precision)
    }

    fun setOutputDir(path: String) {
        outputDir = normalizeOutputDir(path)
    }

    @Throws(IOException::class)
    fun generateFunctionData(
        precision: BigDecimal = PRECISION,
        rangeStart: BigDecimal = NEGATIVE_END,
        rangeEnd: BigDecimal = POSITIVE_END,
        step: BigDecimal = STEP,
    ) {
        buildFunctions().forEach { function ->
            CSVGraphWriter(function, outputDir).write(rangeStart, rangeEnd, step, precision)
        }
    }

    private fun buildFunctions(): List<AbstractFunction> {
        return listOf(
            Sine(),
            Cosine(),
            Secant(),
            Cosecant(),
            Tangent(),
            Cotangent(),
            NaturalLogarithm(),
            BaseNLogarithm(2),
            BaseNLogarithm(3),
            BaseNLogarithm(10),
            mainFunction,
        )
    }

    companion object {
        private const val DEFAULT_OUTPUT_DIR_NAME = "plots"
        val DEFAULT_OUTPUT_DIR: String =
            System.getProperty("user.dir") + File.separator + DEFAULT_OUTPUT_DIR_NAME + File.separator

        val PRECISION: BigDecimal = BigDecimal("0.0000001")
        val POSITIVE_END: BigDecimal = BigDecimal.TEN.setScale(7, HALF_EVEN)
        val NEGATIVE_END: BigDecimal = POSITIVE_END.negate()
        val STEP: BigDecimal = BigDecimal("0.01")

        private fun normalizeOutputDir(path: String): String {
            return if (path.endsWith(File.separator)) path else path + File.separator
        }
    }
}

private data class CliOptions(
    val outputDir: String = App.DEFAULT_OUTPUT_DIR,
    val precision: BigDecimal = App.PRECISION,
    val rangeStart: BigDecimal = App.NEGATIVE_END,
    val rangeEnd: BigDecimal = App.POSITIVE_END,
    val step: BigDecimal = App.STEP,
)

private fun parseArgs(args: Array<String>): CliOptions {
    if (args.isEmpty()) {
        return CliOptions()
    }

    var outputDir: String? = null
    var precision = App.PRECISION
    var rangeStart = App.NEGATIVE_END
    var rangeEnd = App.POSITIVE_END
    var step = App.STEP

    var index = 0
    while (index < args.size) {
        when (val arg = args[index]) {
            "--precision" -> {
                precision = BigDecimal(args.getOrNull(++index) ?: error("missing --precision value"))
            }

            "--start" -> {
                rangeStart = BigDecimal(args.getOrNull(++index) ?: error("missing --start value"))
            }

            "--end" -> {
                rangeEnd = BigDecimal(args.getOrNull(++index) ?: error("missing --end value"))
            }

            "--step" -> {
                step = BigDecimal(args.getOrNull(++index) ?: error("missing --step value"))
            }

            "--help", "-h" -> {
                printUsage()
                kotlin.system.exitProcess(0)
            }

            else -> {
                if (arg.startsWith("--")) {
                    error("bad arg: $arg")
                }
                if (outputDir != null) {
                    error("too many paths")
                }
                outputDir = arg
            }
        }
        index++
    }

    return CliOptions(
        outputDir = outputDir ?: App.DEFAULT_OUTPUT_DIR,
        precision = precision,
        rangeStart = rangeStart,
        rangeEnd = rangeEnd,
        step = step,
    )
}

private fun printUsage() {
    println(
        """
        usage: app [output-dir] [--precision value] [--start value] [--end value] [--step value]
        
        example:
          ./gradlew :app:run --args='app/plots --precision 0.0000001 --start -10 --end 10 --step 0.01'
        """.trimIndent()
    )
}

fun main(args: Array<String>) {
    val options = try {
        parseArgs(args)
    } catch (e: IllegalStateException) {
        System.err.println(e.message)
        printUsage()
        return
    } catch (e: NumberFormatException) {
        System.err.println("bad number: ${e.message}")
        printUsage()
        return
    }

    val app = App(options.outputDir)

    try {
        app.generateFunctionData(
            precision = options.precision,
            rangeStart = options.rangeStart,
            rangeEnd = options.rangeEnd,
            step = options.step,
        )
        println("csv files: ${options.outputDir}")
    } catch (e: IOException) {
        System.err.println("csv write failed: ${e.message}")
        e.printStackTrace()
    }
}
