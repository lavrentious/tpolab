package tpo.lab3.pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.Duration

open class BasePage(
    protected val driver: WebDriver,
    timeout: Duration = Duration.ofSeconds(20),
) {
    protected val wait = WebDriverWait(driver, timeout)

    protected fun waitUntilVisible(locator: By): WebElement =
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator))

    protected fun waitUntilClickable(locator: By): WebElement =
        wait.until(ExpectedConditions.elementToBeClickable(locator))

    protected fun clickElement(element: WebElement) {
        try {
            element.click()
        } catch (_: WebDriverException) {
            (driver as JavascriptExecutor).executeScript("arguments[0].click();", element)
        }
    }

    protected fun triggerElementClick(element: WebElement) {
        (driver as JavascriptExecutor).executeScript(
            """
            const element = arguments[0];
            if (window.jQuery) {
                window.jQuery(element).trigger('click');
                return;
            }
            element.dispatchEvent(new MouseEvent('click', {
                bubbles: true,
                cancelable: true,
                view: window
            }));
            """.trimIndent(),
            element,
        )
    }

    protected fun centerMapUsingResultData(element: WebElement) {
        (driver as JavascriptExecutor).executeScript(
            """
            const item = arguments[0];
            const hash = window.top.location.hash.startsWith('#')
                ? window.top.location.hash.substring(1)
                : window.top.location.hash;
            const params = new URLSearchParams(hash);
            params.set('lat', item.dataset.latitude);
            params.set('lon', item.dataset.longitude);
            if (item.dataset.zoom && item.dataset.zoom !== '0') {
                params.set('z', item.dataset.zoom);
            }
            window.top.location.hash = params.toString();
            """.trimIndent(),
            element,
        )
    }

    protected fun waitUntilFrameAvailable(locator: By) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator))
    }

    protected fun <T> withinFrame(frameLocator: By, action: () -> T): T {
        switchToDefaultContent()
        waitUntilFrameAvailable(frameLocator)

        return try {
            action()
        } finally {
            switchToDefaultContent()
        }
    }

    protected fun switchToDefaultContent() {
        driver.switchTo().defaultContent()
    }

    protected fun currentUrl(): String = driver.currentUrl ?: ""

    protected fun waitUntilUrlContains(fragment: String) {
        wait.until { webDriver ->
            val decodedUrl = URLDecoder.decode(webDriver.currentUrl ?: "", StandardCharsets.UTF_8)
            decodedUrl.contains(fragment)
        }
    }

    protected fun findVisibleElements(locator: By): List<WebElement> {
        return wait.until { webDriver ->
            val visibleElements = webDriver.findElements(locator).filter(WebElement::isDisplayed)
            visibleElements.takeIf { it.isNotEmpty() }
        } ?: emptyList()
    }
}
