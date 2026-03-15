package tpo.lab2.trig.integration

import java.math.BigDecimal
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvFileSource
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tpo.lab2.trig.Cosine
import tpo.lab2.trig.Sine
import tpo.lab2.trig.Tangent

@ExtendWith(MockitoExtension::class)
class TangentIntegrationTest {
    private val precision = BigDecimal("0.0000001")

    @Test
    @DisplayName("call sine and cosine")
    fun shouldCallSineAndCosineFunction() {
        val spySin = spy(Sine())
        val spyCos = spy(Cosine())
        val tan = Tangent(spySin, spyCos)
        val x = BigDecimal(972)
        val intermediatePrecision = precision.setScale(precision.scale() + 5, java.math.RoundingMode.HALF_EVEN)
        tan.calculate(x, precision)
        verify(spySin, atLeastOnce()).calculate(x, intermediatePrecision)
        verify(spyCos, atLeastOnce()).calculate(x, intermediatePrecision)
    }

    @ParameterizedTest(name = "mock.tan({0}) = {1}")
    @DisplayName("call tangent")
    @CsvFileSource(resources = ["/integration/tanIT.csv"], numLinesToSkip = 1)
    fun shouldCallTangentFunction(x: BigDecimal, y: BigDecimal) {
        val mockSin = mock<Sine>()
        val mockCos = mock<Cosine>()
        val intermediatePrecision = precision.setScale(precision.scale() + 5, java.math.RoundingMode.HALF_EVEN)
        whenever(mockSin.calculate(x, intermediatePrecision)).thenReturn(BigDecimal.valueOf(kotlin.math.sin(x.toDouble())))
        whenever(mockCos.calculate(x, intermediatePrecision)).thenReturn(BigDecimal.valueOf(kotlin.math.cos(x.toDouble())))
        val tan = Tangent(mockSin, mockCos)
        assertEquals(y, tan.calculate(x, precision))
    }

    @Test
    @DisplayName("tangent throws")
    fun shouldThrowWhenCosineIsZero() {
        val mockSin = mock<Sine>()
        val mockCos = mock<Cosine>()
        val x = BigDecimal.valueOf(Math.PI).divide(BigDecimal.TWO)
        val intermediatePrecision = precision.setScale(precision.scale() + 5, java.math.RoundingMode.HALF_EVEN)
        whenever(mockSin.calculate(x, intermediatePrecision)).thenReturn(BigDecimal.ONE)
        whenever(mockCos.calculate(x, intermediatePrecision)).thenReturn(BigDecimal.ZERO)
        val tan = Tangent(mockSin, mockCos)
        assertThrows(ArithmeticException::class.java) { tan.calculate(x, precision) }
    }
}
