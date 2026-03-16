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
import tpo.lab2.trig.Cosine
import tpo.lab2.trig.Secant

@ExtendWith(MockitoExtension::class)
class SecantIntegrationTest {
    private val precision = BigDecimal("0.0000001")

    @Test
    @DisplayName("call cosine")
    fun shouldCallCosineFunction() {
        val spyCos = spy(Cosine())
        val sec = Secant(spyCos)
        val x = BigDecimal(986)
        val intermediatePrecision = precision.setScale(precision.scale() + 12, HALF_EVEN)
        sec.calculate(x, precision)
        verify(spyCos, atLeastOnce()).calculate(x, intermediatePrecision)
    }

    @ParameterizedTest(name = "mock.sec({0}) = {1}")
    @DisplayName("call secant")
    @CsvFileSource(resources = ["/integration/secIT.csv"], numLinesToSkip = 1)
    fun shouldCallSecantFunction(x: BigDecimal, y: BigDecimal) {
        val mockCos = mock<Cosine>()
        whenever(mockCos.calculate(x, precision.setScale(precision.scale() + 12, HALF_EVEN)))
            .thenReturn(BigDecimal.valueOf(kotlin.math.cos(x.toDouble())))
        val sec = Secant(mockCos)
        assertEquals(y, sec.calculate(x, precision))
    }
}
