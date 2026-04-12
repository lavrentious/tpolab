package tpo.lab3.pages

import org.openqa.selenium.By
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class SearchResultsFrame(driver: WebDriver) : BasePage(driver) {
    private val frameLocator = By.xpath("//iframe[starts-with(@name, 'searchFrame-')]")
    private val resultRows = By.xpath("//li[contains(@class, 'search-result-item')]")

    fun waitUntilLoaded(): SearchResultsFrame {
        withinFrame(frameLocator) {
            findVisibleElements(resultRows)
        }
        return this
    }

    fun hasAnyResults(): Boolean =
        try {
            withinFrame(frameLocator) {
                waitUntilVisible(firstResultLocator())
            }
            true
        } catch (_: TimeoutException) {
            false
        }

    fun hasResult(title: String, subtitle: String? = null): Boolean =
        try {
            withinFrame(frameLocator) {
                waitUntilVisible(resultLocator(title, subtitle))
            }
            true
        } catch (_: TimeoutException) {
            false
        }

    fun selectFirstResult(): Coordinates =
        withinFrame(frameLocator) {
            val result = waitUntilClickable(firstResultLocator())
            selectResult(result)
        }

    fun selectResult(title: String, subtitle: String? = null): Coordinates =
        withinFrame(frameLocator) {
            val result = waitUntilClickable(resultLocator(title, subtitle))
            selectResult(result)
        }

    private fun selectResult(result: WebElement): Coordinates {
        val selectedCoordinates = coordinatesFromResult(result)
        centerMapUsingResultData(result)
        return selectedCoordinates
    }

    private fun resultLocator(title: String, subtitle: String?): By {
        val escapedTitle = escapeXpathLiteral(title)
        val titlePredicate = ".//strong[normalize-space() = $escapedTitle]"
        val subtitlePredicate = subtitle?.let {
            " and .//*[contains(normalize-space(), ${escapeXpathLiteral(it)})]"
        }.orEmpty()

        return By.xpath("//li[contains(@class, 'search-result-item')][$titlePredicate$subtitlePredicate]")
    }

    private fun firstResultLocator(): By =
        By.xpath("(//li[contains(@class, 'search-result-item')])[1]")

    private fun coordinatesFromResult(result: WebElement): Coordinates =
        Coordinates(
            latitude = requireNotNull(result.getAttribute("data-latitude")).toDouble(),
            longitude = requireNotNull(result.getAttribute("data-longitude")).toDouble(),
        )

    private fun escapeXpathLiteral(value: String): String {
        if (!value.contains("'")) {
            return "'$value'"
        }

        return value.split("'").joinToString(", \"'\", ", prefix = "concat(", postfix = ")") { "'$it'" }
    }
}
