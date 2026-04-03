package tpo.lab3.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class WikimapiaHomePage(
    driver: WebDriver,
    private val baseUrl: String,
) : BasePage(driver) {
    private val searchInput = By.xpath("//input[@id='search-input' or @type='text']")
    private val searchSubmitButton = By.xpath("//button[@id='search-submit']")

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

    private fun buildMapUrl(lat: Double, lon: Double): String {
        return "$baseUrl/#lang=en&lat=$lat&lon=$lon&z=15&m=w"
    }
}
