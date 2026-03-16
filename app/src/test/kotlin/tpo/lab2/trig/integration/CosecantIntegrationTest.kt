package tpo.lab2.trig.integration

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertEquals
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
import tpo.lab2.trig.Cosecant
import tpo.lab2.trig.Sine

@ExtendWith(MockitoExtension::class)
class CosecantIntegrationTest {
    private val precision = BigDecimal("0.0000001")

    @Test
    @DisplayName("call sine")
    fun shouldCallSineFunction() {
        val spySin = spy(Sine())
        val csc = Cosecant(spySin)
        val x = BigDecimal(965)
        val intermediatePrecision = precision.setScale(precision.scale() + 12, HALF_EVEN)
        csc.calculate(x, precision)
        verify(spySin, atLeastOnce()).calculate(x, intermediatePrecision)
    }

    @ParameterizedTest(name = "mock.csc({0}) = {1}")
    @DisplayName("call cosecant")
    @CsvFileSource(resources = ["/integration/cscIT.csv"], numLinesToSkip = 1)
    fun shouldCallCosecantFunction(x: BigDecimal, y: BigDecimal) {
        val mockSin = mock<Sine>()
        whenever(mockSin.calculate(x, precision.setScale(precision.scale() + 12, HALF_EVEN)))
            .thenReturn(BigDecimal.valueOf(kotlin.math.sin(x.toDouble())))
        val csc = Cosecant(mockSin)
        assertEquals(y, csc.calculate(x, precision))
    }
}
