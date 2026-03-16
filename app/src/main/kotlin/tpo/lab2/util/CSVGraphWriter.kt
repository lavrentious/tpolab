package tpo.lab2.util

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.math.BigDecimal
import java.util.Locale
import tpo.lab2.function.AbstractFunction

class CSVGraphWriter(
    private val writer: BufferedWriter,
    outputDir: String,
    private val function: AbstractFunction,
) {
    val filePath: String = getFilePath(outputDir, function)

    constructor(function: AbstractFunction, outputDir: String) : this(
        writer = createWriter(getFilePath(outputDir, function)),
        outputDir = outputDir,
        function = function,
    )

    @Throws(IOException::class)
    fun write(x1: BigDecimal, x2: BigDecimal, d: BigDecimal, precision: BigDecimal) {
        try {
            writer.write("x,y")
            writer.newLine()

            var current = x1
            while (current <= x2) {
                try {
                    val y = function.calculate(current, precision)
                    writer.write("${current.toPlainString()},${y.toPlainString()}")
                } catch (_: ArithmeticException) {
                    writer.write("${current.toPlainString()},NaN")
                }
                writer.newLine()

                current = current.add(d)
            }
        } finally {
            writer.flush()
        }
    }

    companion object {
        private fun getFilePath(outputDir: String, function: AbstractFunction): String {
            return outputDir + function::class.java.simpleName + ".csv"
        }

        private fun createWriter(filePath: String): BufferedWriter {
            val file = File(filePath)
            file.parentFile?.mkdirs()

            return if (file.exists()) {
                BufferedWriter(FileWriter(file, false))
            } else {
                file.createNewFile()
                BufferedWriter(FileWriter(file))
            }
        }
    }
}
