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
import tpo.lab2.trig.Cotangent
import tpo.lab2.trig.Sine

@ExtendWith(MockitoExtension::class)
class CotangentIntegrationTest {
    private val precision = BigDecimal("0.0000001")

    @Test
    @DisplayName("call sine and cosine")
    fun shouldCallSineAndCosineFunction() {
        val spySin = spy(Sine())
        val spyCos = spy(Cosine())
        val cot = Cotangent(spySin, spyCos)
        val x = BigDecimal(979)
        val intermediatePrecision = precision.setScale(precision.scale() + 5, java.math.RoundingMode.HALF_EVEN)
        cot.calculate(x, precision)
        verify(spySin, atLeastOnce()).calculate(x, intermediatePrecision)
        verify(spyCos, atLeastOnce()).calculate(x, intermediatePrecision)
    }

    @ParameterizedTest(name = "mock.cot({0}) = {1}")
    @DisplayName("call cotangent")
    @CsvFileSource(resources = ["/integration/cotIT.csv"], numLinesToSkip = 1)
    fun shouldCallCotangentFunction(x: BigDecimal, y: BigDecimal) {
        val mockSin = mock<Sine>()
        val mockCos = mock<Cosine>()
        val intermediatePrecision = precision.setScale(precision.scale() + 5, java.math.RoundingMode.HALF_EVEN)
        whenever(mockSin.calculate(x, intermediatePrecision)).thenReturn(BigDecimal.valueOf(kotlin.math.sin(x.toDouble())))
        whenever(mockCos.calculate(x, intermediatePrecision)).thenReturn(BigDecimal.valueOf(kotlin.math.cos(x.toDouble())))
        val cot = Cotangent(mockSin, mockCos)
        assertEquals(y, cot.calculate(x, precision))
    }

    @Test
    @DisplayName("cotangent throws")
    fun shouldThrowWhenSineIsZero() {
        val mockSin = mock<Sine>()
        val mockCos = mock<Cosine>()
        val x = BigDecimal.valueOf(Math.PI)
        val intermediatePrecision = precision.setScale(precision.scale() + 5, java.math.RoundingMode.HALF_EVEN)
        whenever(mockSin.calculate(x, intermediatePrecision)).thenReturn(BigDecimal.ZERO)
        whenever(mockCos.calculate(x, intermediatePrecision)).thenReturn(BigDecimal.ONE)
        val cot = Cotangent(mockSin, mockCos)
        assertThrows(ArithmeticException::class.java) { cot.calculate(x, precision) }
    }
}
