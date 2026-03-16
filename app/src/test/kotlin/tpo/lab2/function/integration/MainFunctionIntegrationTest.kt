package tpo.lab2.function.integration

import java.math.BigDecimal
import java.math.RoundingMode.HALF_EVEN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import tpo.lab2.function.MainFunction
import tpo.lab2.log.BaseNLogarithm
import tpo.lab2.log.NaturalLogarithm
import tpo.lab2.trig.*

@ExtendWith(MockitoExtension::class)
class MainFunctionIntegrationTest {
    private val precision = BigDecimal("0.0000001")

    private val cosMock = mock<Cosine>()
    private val tanMock = mock<Tangent>()
    private val cotMock = mock<Cotangent>()
    private val secMock = mock<Secant>()
    private val cscMock = mock<Cosecant>()
    private val lnMock = mock<NaturalLogarithm>()
    private val log3Mock = mock<BaseNLogarithm>()
    private val log5Mock = mock<BaseNLogarithm>()
    private val log10Mock = mock<BaseNLogarithm>()

    private lateinit var mainFunction: MainFunction

    @BeforeEach
    fun setup() {
        mainFunction = MainFunction(
            cosMock, tanMock, cotMock, secMock, cscMock,
            lnMock, log3Mock, log5Mock, log10Mock
        )
    }

    @Test
    fun shouldCalculateRightBranch() {
        val x = BigDecimal("2.0")

        whenever(lnMock.calculate(eq(x), any())).thenReturn(BigDecimal("2.0000000000000000000"))
        whenever(log3Mock.calculate(eq(x), any())).thenReturn(BigDecimal("4.0000000000000000000"))
        whenever(log5Mock.calculate(eq(x), any())).thenReturn(BigDecimal("1.0000000000000000000"))
        whenever(log10Mock.calculate(eq(x), any())).thenReturn(BigDecimal("8.0000000000000000000"))

        val result = mainFunction.calculate(x, precision)

        // (((4/2)^2 + 1) / (8 / 2^3))^3 = ((4+1) / (8/8))^3 = 125
        assertEquals(BigDecimal("125.0000000"), result)
    }

    @Test
    fun shouldThrowExceptionAt1() {
        // log branch division by 0
        val x = BigDecimal.ONE
        whenever(lnMock.calculate(eq(x), any())).thenReturn(BigDecimal.ZERO)
        whenever(log3Mock.calculate(eq(x), any())).thenReturn(BigDecimal.ONE)
        whenever(log5Mock.calculate(eq(x), any())).thenReturn(BigDecimal.ONE)
        whenever(log10Mock.calculate(eq(x), any())).thenReturn(BigDecimal.ONE)

        assertThrows(ArithmeticException::class.java) {
            mainFunction.calculate(x, precision)
        }
    }

    @Test
    fun shouldCalculateLeftBranch() {
        val x = BigDecimal("-1.0")

        whenever(cosMock.calculate(eq(x), any())).thenReturn(BigDecimal("0.5"))
        whenever(tanMock.calculate(eq(x), any())).thenReturn(BigDecimal("0.2"))
        whenever(cotMock.calculate(eq(x), any())).thenReturn(BigDecimal("5.0"))
        whenever(secMock.calculate(eq(x), any())).thenReturn(BigDecimal("2.0"))
        whenever(cscMock.calculate(eq(x), any())).thenReturn(BigDecimal("1.0"))

        assertDoesNotThrow {
            mainFunction.calculate(x, precision)
        }
    }
}
