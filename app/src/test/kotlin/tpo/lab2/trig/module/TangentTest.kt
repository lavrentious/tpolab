package tpo.lab2.trig.module

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.junit.jupiter.params.provider.ValueSource
import tpo.lab2.trig.Tangent

class TangentTest {
    private val precision = BigDecimal("0.0000001")
    private lateinit var tan: Tangent

    @BeforeEach
    fun init() {
        tan = Tangent()
    }

    @ParameterizedTest(name = "tan({0})")
    @ValueSource(doubles = [-Math.PI, 0.0, Math.PI])
    fun shouldCalculateForPi(x: Double) {
        assertEquals(BigDecimal.ZERO.setScale(precision.scale(), HALF_EVEN), tan.calculate(BigDecimal.valueOf(x), precision))
    }

    @ParameterizedTest
    @ValueSource(doubles = [-Math.PI / 2, Math.PI / 2])
    fun shouldNotCalculateForPiHalf(x: Double) {
        val arg = BigDecimal.valueOf(x).setScale(precision.scale(), HALF_EVEN)
        val exception = assertThrows(ArithmeticException::class.java) { tan.calculate(arg, precision) }
        assertEquals("tan: bad x = $arg", exception.message)
    }

    @ParameterizedTest(name = "tan({0})")
    @CsvFileSource(resources = ["/tan.csv"], numLinesToSkip = 1)
    fun testTan(x: BigDecimal, y: BigDecimal) {
        assertEquals(y, tan.calculate(x, precision))
    }
}
