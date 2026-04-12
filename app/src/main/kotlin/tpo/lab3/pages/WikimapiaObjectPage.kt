package tpo.lab3.pages

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver

class WikimapiaObjectPage(
    driver: WebDriver,
    private val baseUrl: String,
) : BasePage(driver) {
    private val titleLocator = By.tagName("h1")
    private val photoLinksLocator = By.xpath("//a[contains(@href, 'photos.wikimapia.org')]")
    private val descriptionLocator =
        By.xpath("//*[contains(normalize-space(), 'The Tomb of the Unknown Soldier') and contains(normalize-space(), 'Mogila Neizvestnogo Soldata')]")
    private val dialogLocator = By.xpath("//div[@role='dialog' or self::dialog]")
    private val dialogCloseButton = By.xpath("//div[@role='dialog' or self::dialog]//button[normalize-space()='×']")

    fun open(objectPath: String): WikimapiaObjectPage {
        driver.get("$baseUrl$objectPath")
        waitUntilVisible(titleLocator)
        return this
    }

    fun title(): String = waitUntilVisible(titleLocator).text.trim()

    fun categories(): List<String> =
        ((driver as JavascriptExecutor).executeScript(
            """
            return Array.from(document.querySelectorAll('strong'))
                .map(element => element.textContent.trim())
                .filter(text => text && text !== 'Wikipedia article:');
            """.trimIndent(),
        ) as List<*>)
            .map { it.toString() }
            .filter(String::isNotEmpty)

    fun photoCount(): Int = findVisibleElements(photoLinksLocator).size

    fun description(): String = waitUntilVisible(descriptionLocator).text.trim()

    fun isCommentDialogVisible(): Boolean =
        driver.findElements(dialogLocator).any { it.isDisplayed && it.text.contains("Post comment") }

    fun closeCommentDialog(): WikimapiaObjectPage {
        clickElement(waitUntilClickable(dialogCloseButton))
        wait.until { webDriver -> webDriver.findElements(dialogLocator).none { it.isDisplayed } }
        return this
    }
}
