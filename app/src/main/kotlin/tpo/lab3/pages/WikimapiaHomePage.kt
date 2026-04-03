package tpo.lab3.pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions

class WikimapiaHomePage(
    driver: WebDriver,
    private val baseUrl: String,
) : BasePage(driver) {
    private val searchInput = By.xpath("//input[@id='search-input' or @type='text']")
    private val searchSubmitButton = By.xpath("//button[@id='search-submit']")
    private val mapSwitcherButton = By.xpath("//*[@id='wm-button-27' or @id='wm-MapSwitcher']")
    private val satelliteOption =
        By.xpath("//*[contains(concat(' ', normalize-space(@class), ' '), ' switcher-preview ') and contains(concat(' ', normalize-space(@class), ' '), ' google-satellite-preview ')]")

    fun openAtCenter(lat: Double, lon: Double): WikimapiaHomePage {
        driver.get(buildMapUrl(lat, lon))
        waitUntilVisible(searchInput)
        return this
    }

    fun search(query: String): SearchResultsFrame {
        val input = waitUntilVisible(searchInput)
        input.clear()
        input.sendKeys(query)

        waitUntilClickable(searchSubmitButton).click()

        return SearchResultsFrame(driver)
    }

    fun currentCoordinates(): Coordinates = Coordinates.fromUrl(currentUrl())

    fun currentMapMode(): String {
        val fragment = currentUrl().substringAfter('#', "")
        return fragment.split('&')
            .firstOrNull { it.startsWith("m=") }
            ?.substringAfter('=')
            .orEmpty()
    }

    fun selectedLayerPreviewClass(): String =
        ((driver as JavascriptExecutor).executeScript(
            """
            const selected = document.evaluate(
                "//*[contains(concat(' ', normalize-space(@class), ' '), ' switcher-preview ') and contains(concat(' ', normalize-space(@class), ' '), ' selected ')]",
                document,
                null,
                XPathResult.FIRST_ORDERED_NODE_TYPE,
                null
            ).singleNodeValue;
            return selected ? selected.className : '';
            """.trimIndent(),
        )?.toString()).orEmpty()

    fun switchToSatelliteLayer(): WikimapiaHomePage {
        val switcher = waitUntilVisible(mapSwitcherButton)
        Actions(driver).moveToElement(switcher).pause(java.time.Duration.ofMillis(300)).perform()
        val option = wait.until { webDriver ->
            webDriver.findElements(satelliteOption).firstOrNull()
        } ?: error("Expected the satellite layer option to be present in the layer switcher.")

        triggerElementClick(option)
        wait.until { selectedLayerPreviewClass().contains("google-satellite-preview") }
        return this
    }

    private fun buildMapUrl(lat: Double, lon: Double): String {
        return "$baseUrl/#lang=en&lat=$lat&lon=$lon&z=15&m=w"
    }
}
