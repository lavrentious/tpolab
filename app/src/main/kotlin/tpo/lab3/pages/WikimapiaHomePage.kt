package tpo.lab3.pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

class WikimapiaHomePage(
    driver: WebDriver,
    private val baseUrl: String,
) : BasePage(driver) {
    private val searchInput = By.xpath("//input[@id='search-input' or @type='text']")
    private val searchSubmitButton = By.xpath("//button[@id='search-submit']")
    private val mapSwitcherButton = By.xpath("//*[@id='wm-button-27' or @id='wm-MapSwitcher']")
    private val zoomInButton = By.id("wm-zoomControl-zoom-in")
    private val satelliteOption =
        By.xpath("//*[contains(concat(' ', normalize-space(@class), ' '), ' switcher-preview ') and contains(concat(' ', normalize-space(@class), ' '), ' google-satellite-preview ')]")
    private val languageMenuToggle =
        By.xpath("//a[normalize-space()='EN' or normalize-space()='en' or normalize-space()='English'] | //span[normalize-space()='EN' or normalize-space()='en' or normalize-space()='English']")
    private val moreLanguagesOption =
        By.xpath("//*[self::a or self::button or self::span or self::div or self::li][contains(translate(normalize-space(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'more languages')]")

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

    fun currentZoomLevel(): Int {
        val fragment = currentUrl().substringAfter('#', "")
        return fragment.split('&')
            .firstOrNull { it.startsWith("z=") }
            ?.substringAfter('=')
            ?.toIntOrNull()
            ?: error("zoom missing in url: ${currentUrl()}")
    }

    fun currentMapMode(): String {
        val fragment = currentUrl().substringAfter('#', "")
        return fragment.split('&')
            .firstOrNull { it.startsWith("m=") }
            ?.substringAfter('=')
            .orEmpty()
    }

    fun currentLanguage(): String {
        val fragment = currentUrl().substringAfter('#', "")
        return fragment.split('&')
            .firstOrNull { it.startsWith("lang=") }
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
        } ?: error("satellite option missing")

        triggerElementClick(option)
        wait.until { selectedLayerPreviewClass().contains("google-satellite-preview") }
        return this
    }

    fun zoomIn(): WikimapiaHomePage {
        val initialZoomLevel = currentZoomLevel()
        clickElement(waitUntilClickable(zoomInButton))
        wait.until { currentZoomLevel() > initialZoomLevel }
        return this
    }

    fun addPlaceLabel(): String =
        wait.until {
            ((driver as JavascriptExecutor).executeScript(
                """
                const candidates = [
                    document.getElementById('wm-button-86-content'),
                    document.getElementById('wm-Add'),
                    document.evaluate(
                        "//*[contains(concat(' ', normalize-space(@class), ' '), ' add-place ')]//*[contains(concat(' ', normalize-space(@class), ' '), ' button-text ')]",
                        document,
                        null,
                        XPathResult.FIRST_ORDERED_NODE_TYPE,
                        null
                    ).singleNodeValue
                ].filter(Boolean);
                const text = candidates
                    .map(node => (node.textContent || '').replace(/\\s+/g, ' ').trim())
                    .find(value => value.length > 0);
                return text || '';
                """.trimIndent(),
            )?.toString()).orEmpty().takeIf { it.isNotBlank() }
        } ?: error("add place label missing")

    fun switchToRussianLanguage(): WikimapiaHomePage {
        val menuToggle = wait.until { webDriver ->
            webDriver.findElements(languageMenuToggle)
                .firstOrNull { it.isDisplayed }
        } ?: error("language selector missing")

        hoverElement(menuToggle)

        if (!clickVisibleText("more languages", "More languages", "More Languages")) {
            hoverElement(menuToggle)
        }

        if (!clickVisibleTextWithRetry("Russian", "Русский")) {
            if (!addPlaceLabel().contains("Добавить")) {
                error("language switch failed")
            }
        }

        wait.until { addPlaceLabel().contains("Добавить") }
        return this
    }

    private fun hoverElement(element: WebElement) {
        Actions(driver).moveToElement(element).pause(java.time.Duration.ofMillis(300)).perform()
    }

    private fun clickVisibleTextWithRetry(vararg labels: String, attempts: Int = 10): Boolean {
        repeat(attempts) {
            if (clickVisibleText(*labels)) {
                return true
            }
            Thread.sleep(300)
        }
        return false
    }

    private fun visibleTextSnapshot(): List<String> =
        ((driver as JavascriptExecutor).executeScript(
            """
            return Array.from(document.querySelectorAll('a, button, span, div, li'))
                .map(element => ({
                    text: (element.textContent || '').replace(/\s+/g, ' ').trim(),
                    width: element.getBoundingClientRect().width,
                    height: element.getBoundingClientRect().height,
                    visibility: window.getComputedStyle(element).visibility,
                    display: window.getComputedStyle(element).display
                }))
                .filter(item => item.text && item.width > 0 && item.height > 0 && item.visibility !== 'hidden' && item.display !== 'none')
                .map(item => item.text)
                .filter((text, index, all) => all.indexOf(text) === index)
                .slice(0, 80);
            """.trimIndent(),
        ) as List<*>).map { it.toString() }

    private fun clickVisibleText(vararg labels: String): Boolean =
        (driver as JavascriptExecutor).executeScript(
            """
            const labels = Array.from(arguments).map(label => label.toLowerCase());
            const candidates = Array.from(document.querySelectorAll('a, button, span, div, li'))
                .filter(element => {
                    const text = (element.textContent || '').replace(/\s+/g, ' ').trim().toLowerCase();
                    const rect = element.getBoundingClientRect();
                    const style = window.getComputedStyle(element);
                    return labels.some(label => text === label || text.includes(label)) &&
                        rect.width > 0 &&
                        rect.height > 0 &&
                        style.visibility !== 'hidden' &&
                        style.display !== 'none';
                })
                .map(element => ({
                    element,
                    text: (element.textContent || '').replace(/\s+/g, ' ').trim().toLowerCase()
                }));
            const exact = candidates
                .filter(candidate => labels.includes(candidate.text))
                .sort((left, right) => left.text.length - right.text.length);
            const partial = candidates
                .filter(candidate => labels.some(label => candidate.text.includes(label)))
                .sort((left, right) => left.text.length - right.text.length);
            const target = (exact[0] || partial[0] || {}).element;
            if (!target) {
                return false;
            }
            target.dispatchEvent(new MouseEvent('click', {
                bubbles: true,
                cancelable: true,
                view: window
            }));
            return true;
            """.trimIndent(),
            *labels,
        ) as Boolean

    private fun buildMapUrl(lat: Double, lon: Double, lang: String = "en"): String {
        return "$baseUrl/#lang=$lang&lat=$lat&lon=$lon&z=15&m=w"
    }
}
