package tpo.lab2.util

import java.io.BufferedWriter
import java.io.File
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tpo.lab2.function.AbstractFunction

class CSVGraphWriterTest {
    private val defaultDir = System.getProperty("user.dir") + File.separator + "plots" + File.separator

    private var writer: CSVGraphWriter? = null
    private var mockFunction: AbstractFunction = mock()

    @Test
    fun shouldCreateFile() {
        writer = CSVGraphWriter(mockFunction, defaultDir)
        val expectedFile = File(defaultDir + mockFunction::class.java.simpleName + ".csv")

        assertTrue(expectedFile.exists(), "file exists")
    }

    @Test
    fun shouldBuildFilePathAndCreateParentDirectories() {
        val nestedDir = Path.of(defaultDir, "deep", "path").toString() + File.separator
        val expectedFile = File(nestedDir + mockFunction::class.java.simpleName + ".csv")

        assertTrue(!expectedFile.parentFile.exists(), "no parent dir")

        writer = CSVGraphWriter(mockFunction, nestedDir)

        assertEquals(expectedFile.path, writer!!.filePath)
        assertTrue(expectedFile.parentFile.exists(), "has parent dir")
        assertTrue(expectedFile.exists(), "has file")
    }

    @Test
    fun shouldReuseExistingFileInNestedDirectory() {
        val nestedDir = Path.of(defaultDir, "nested", "graphs").toString() + File.separator
        val existingFile = File(nestedDir + mockFunction::class.java.simpleName + ".csv")
        existingFile.parentFile.mkdirs()
        existingFile.writeText("stale")

        whenever(mockFunction.calculate(BigDecimal.ZERO, BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)

        writer = CSVGraphWriter(mockFunction, nestedDir)
        writer!!.write(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.valueOf(0.01))

        val lines = Files.readAllLines(existingFile.toPath())
        assertEquals(listOf("x,y", "0,1"), lines) 
    }

    @Test
    fun shouldWriteToFile() {
        whenever(mockFunction.calculate(BigDecimal.ZERO, BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.ONE, BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.TWO, BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.valueOf(3), BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.valueOf(4), BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.valueOf(5), BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.valueOf(6), BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.valueOf(7), BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.valueOf(8), BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.valueOf(9), BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)
        whenever(mockFunction.calculate(BigDecimal.TEN, BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ONE)

        writer = CSVGraphWriter(mockFunction, defaultDir)
        writer!!.write(BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ONE, BigDecimal.valueOf(0.01))

        val file = File(defaultDir + mockFunction::class.java.simpleName + ".csv")
        val lines = Files.readAllLines(file.toPath())

        assertEquals("x,y", lines[0])
        assertEquals("0,1", lines[1]) 
    }

    @Test
    fun shouldHandleArithmeticException() {
        whenever(mockFunction.calculate(BigDecimal.ZERO, BigDecimal.valueOf(0.01)))
            .thenThrow(ArithmeticException("gap"))
        whenever(mockFunction.calculate(BigDecimal.ONE, BigDecimal.valueOf(0.01)))
            .thenThrow(ArithmeticException("gap"))

        writer = CSVGraphWriter(mockFunction, defaultDir)
        writer!!.write(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.valueOf(0.01))

        val file = File(defaultDir + mockFunction::class.java.simpleName + ".csv")
        val lines = Files.readAllLines(file.toPath())

        assertEquals("0,NaN", lines[1])
        assertEquals("x,y", lines[0])
    }

    @Test
    fun shouldCallFlush() {
        val function = mock<AbstractFunction>()
        whenever(function.calculate(BigDecimal.ZERO, BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ZERO)
        whenever(function.calculate(BigDecimal.ONE, BigDecimal.valueOf(0.01))).thenReturn(BigDecimal.ZERO)

        val mockWriter = mock<BufferedWriter>()

        writer = CSVGraphWriter(mockWriter, defaultDir, function)
        writer!!.write(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.valueOf(0.01))

        verify(mockWriter, atLeastOnce()).flush()
    }

    @AfterEach
    fun tearDown() {
        writer = null
        val plotsDir = File(defaultDir)
        if (plotsDir.exists()) {
            plotsDir.deleteRecursively()
        }
    }
}
