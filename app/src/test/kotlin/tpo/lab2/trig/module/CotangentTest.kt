package tpo.lab2.trig.module

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.junit.jupiter.params.provider.ValueSource
import tpo.lab2.trig.Cotangent

class CotangentTest {
    private val precision = BigDecimal("0.0000001")
    private lateinit var cot: Cotangent

    @BeforeEach
    fun init() {
        cot = Cotangent()
    }

    @ParameterizedTest(name = "cot({0})")
    @ValueSource(doubles = [-1.5 * Math.PI, -Math.PI / 2, Math.PI / 2, 1.5 * Math.PI])
    fun shouldCalculateForPiHalf(x: Double) {
        assertEquals(
            BigDecimal.ZERO.setScale(precision.scale(), HALF_EVEN),
            cot.calculate(BigDecimal.valueOf(x), precision),
        )
    }

    @ParameterizedTest
    @ValueSource(doubles = [-Math.PI, 0.0, Math.PI])
    fun shouldNotCalculateForPi(x: Double) {
        val arg = BigDecimal.valueOf(x).setScale(precision.scale(), HALF_EVEN)
        val exception = assertThrows(ArithmeticException::class.java) { cot.calculate(arg, precision) }
        assertEquals("cot: bad x = $arg", exception.message)
    }

    @ParameterizedTest(name = "cot({0})")
    @CsvFileSource(resources = ["/cot.csv"], numLinesToSkip = 1)
    fun testCot(x: BigDecimal, y: BigDecimal) {
        assertEquals(y, cot.calculate(x, precision))
    }
}
