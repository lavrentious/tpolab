package tpo.lab3.pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

class WikimapiaMapObjectInfoPage(
    driver: WebDriver,
    private val baseUrl: String,
) : BasePage(driver) {
    private val searchInput = By.id("search-input")
    private val polygonLocator = By.xpath("//*[@id='vector-root']//*[name()='path' and @cursor='pointer']")
    private val tooltipLocator = By.id("wm-tooltip")
    private val infoPanelLocator =
        By.xpath("//*[contains(concat(' ', normalize-space(@class), ' '), ' wm-panel ') and contains(concat(' ', normalize-space(@class), ' '), ' panel-left ')]")
    private val infoPanelFrameLocator =
        By.xpath("//*[contains(concat(' ', normalize-space(@class), ' '), ' wm-panel ') and contains(concat(' ', normalize-space(@class), ' '), ' panel-left ')]//iframe")
    private val infoPanelCloseButton =
        By.xpath("//*[contains(concat(' ', normalize-space(@class), ' '), ' wm-panel ') and contains(concat(' ', normalize-space(@class), ' '), ' panel-left ')]//*[contains(concat(' ', normalize-space(@class), ' '), ' control-button ') and contains(concat(' ', normalize-space(@class), ' '), ' close ')]")
    private val infoTitleLocator = By.tagName("h1")
    private val photoLinksLocator = By.xpath("//a[contains(@href, 'photos.wikimapia.org')]")
    private val categoriesLocator = By.tagName("strong")

    fun openAtCenter(lat: Double, lon: Double): WikimapiaMapObjectInfoPage {
        driver.get("$baseUrl/#lang=en&lat=$lat&lon=$lon&z=17&m=w")
        waitUntilVisible(searchInput)
        return this
    }

    fun openInfoBox(expectedTooltip: String, objectPath: String): WikimapiaMapObjectInfoPage {
        if (!tryOpenInfoBoxByClick(expectedTooltip)) {
            openInfoBoxFromHash(objectPath)
        }
        return this
    }

    fun isInfoBoxOpen(): Boolean =
        driver.findElements(infoPanelLocator).any { it.isDisplayed }

    fun infoBoxTitle(): String =
        withinInfoFrame {
            waitUntilVisible(infoTitleLocator).text.normalizeWhitespace()
        }

    fun infoBoxDescription(): String =
        withinInfoFrame {
            val description = (driver as org.openqa.selenium.JavascriptExecutor).executeScript(
                """
                const textBlocks = Array.from(document.querySelectorAll('div, p, td'))
                    .map(element => (element.textContent || '').replace(/\s+/g, ' ').trim())
                    .filter(text =>
                        text.length >= 120 &&
                        !text.includes('Wikimapia is a multilingual') &&
                        !text.startsWith('if (') &&
                        !text.includes('Nearby cities') &&
                        !text.includes('Add your comment')
                    );
                return textBlocks[0] || '';
                """.trimIndent(),
            )?.toString().orEmpty()

            description.normalizeWhitespace()
        }

    fun infoBoxPhotoCount(): Int =
        withinInfoFrame {
            findVisibleElements(photoLinksLocator).size
        }

    fun infoBoxCategories(): List<String> =
        withinInfoFrame {
            driver.findElements(categoriesLocator)
                .map { it.text.normalizeWhitespace() }
                .filter { it.isNotEmpty() && it != "Wikipedia article:" }
        }

    fun closeInfoBox(): WikimapiaMapObjectInfoPage {
        clickElement(waitUntilClickable(infoPanelCloseButton))
        wait.until { webDriver ->
            webDriver.findElements(infoPanelLocator).none { it.isDisplayed }
        }
        wait.until { !currentUrl().contains("show=/") }
        return this
    }

    private fun <T> withinInfoFrame(action: () -> T): T {
        return withinFrame(infoPanelFrameLocator, action)
    }

    private fun tryOpenInfoBoxByClick(expectedTooltip: String): Boolean {
        val polygonsAvailable = try {
            wait.until {
                ((driver as JavascriptExecutor).executeScript(
                    "return document.evaluate(\"count(//*[@id='vector-root']//*[name()='path' and @cursor='pointer'])\", document, null, XPathResult.NUMBER_TYPE, null).numberValue;",
                ) as Number).toInt() > 0
            }
        } catch (_: TimeoutException) {
            false
        }

        if (!polygonsAvailable) {
            return false
        }

        val polygon = try {
            wait.until { webDriver ->
                webDriver.findElements(polygonLocator)
                    .firstOrNull { candidate -> matchesTooltip(candidate, expectedTooltip) }
            }
        } catch (_: TimeoutException) {
            null
        } ?: return false

        triggerElementClick(polygon)
        return waitForInfoBoxToOpen()
    }

    private fun openInfoBoxFromHash(objectPath: String) {
        val currentHash = currentUrl().substringAfter('#', "")
        val params = currentHash
            .split('&')
            .filter { it.isNotBlank() && !it.startsWith("show=") }
            .toMutableList()
        params.add("show=$objectPath")

        driver.get("$baseUrl/#${params.joinToString("&")}")

        check(waitForInfoBoxToOpen()) {
            "Expected the map to open an info box for '$objectPath'."
        }
    }

    private fun waitForInfoBoxToOpen(): Boolean =
        try {
            wait.until { currentUrl().contains("show=/") }
            wait.until { webDriver ->
                webDriver.findElements(infoPanelLocator).any { it.isDisplayed }
            }
            true
        } catch (_: TimeoutException) {
            false
        }

    private fun matchesTooltip(polygon: WebElement, expectedTooltip: String): Boolean {
        (driver as JavascriptExecutor).executeScript(
            """
            const element = arguments[0];
            const rect = element.getBoundingClientRect();
            const options = {
                bubbles: true,
                cancelable: true,
                clientX: rect.left + rect.width / 2,
                clientY: rect.top + rect.height / 2,
                view: window
            };
            ['mouseenter', 'mouseover', 'mousemove'].forEach(type => {
                element.dispatchEvent(new MouseEvent(type, options));
            });
            """.trimIndent(),
            polygon,
        )

        return wait.until {
            driver.findElement(tooltipLocator).text.normalizeWhitespace()
        } == expectedTooltip
    }

    private fun String.normalizeWhitespace(): String = trim().replace("\\s+".toRegex(), " ")
}
