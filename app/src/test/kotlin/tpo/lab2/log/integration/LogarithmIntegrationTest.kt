package tpo.lab2.log.integration

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tpo.lab2.log.BaseNLogarithm
import tpo.lab2.log.NaturalLogarithm

@ExtendWith(MockitoExtension::class)
class LogarithmIntegrationTest {
    private val precision = BigDecimal("0.0000001")

    @Test
    fun shouldCallLn() {
        val spyLn = spy(NaturalLogarithm())
        val logarithm = BaseNLogarithm(5, spyLn)
        val inputPrecision = BigDecimal("0.001")
        val intermediatePrecision = inputPrecision.setScale(inputPrecision.scale() + 4, HALF_EVEN)
        val x = BigDecimal(993)

        logarithm.calculate(x, inputPrecision)

        verify(spyLn, atLeastOnce()).calculate(x, intermediatePrecision)
        verify(spyLn, atLeastOnce()).calculate(BigDecimal.valueOf(5), intermediatePrecision)
    }

    @Test
    fun shouldCalculateWithMockLn() {
        val mockLn = mock<NaturalLogarithm>()
        val arg = BigDecimal(3621)
        val intermediatePrecision = precision.setScale(precision.scale() + 4, HALF_EVEN)
        whenever(mockLn.calculate(arg, intermediatePrecision)).thenReturn(BigDecimal("7.3051882"))
        whenever(mockLn.calculate(BigDecimal.valueOf(5), intermediatePrecision)).thenReturn(BigDecimal("1.6094379"))

        val log5 = BaseNLogarithm(5, mockLn)
        val expected = BigDecimal("4.5389687")

        assertEquals(expected, log5.calculate(arg, precision))
    }
}
