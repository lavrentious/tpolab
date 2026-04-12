package tpo.lab3

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.WebDriver
import tpo.lab3.config.TestConfig
import tpo.lab3.driver.WebDriverFactory
import tpo.lab3.pages.WikimapiaHomePage
import tpo.lab3.pages.WikimapiaMapObjectInfoPage
import tpo.lab3.pages.WikimapiaObjectPage

abstract class BaseUiTest {
    protected lateinit var driver: WebDriver
    protected lateinit var config: TestConfig

    @BeforeEach
    fun setUp() {
        config = TestConfig.fromSystemProperties()
        driver = WebDriverFactory.create(config)
    }

    @AfterEach
    fun tearDown() {
        if (::driver.isInitialized) {
            driver.quit()
        }
    }

    protected fun openHomePageAt(latitude: Double, longitude: Double): WikimapiaHomePage {
        return WikimapiaHomePage(driver, config.baseUrl).openAtCenter(latitude, longitude)
    }

    protected fun openMapObjectInfoPageAt(latitude: Double, longitude: Double): WikimapiaMapObjectInfoPage {
        return WikimapiaMapObjectInfoPage(driver, config.baseUrl).openAtCenter(latitude, longitude)
    }

    protected fun openObjectPage(objectPath: String): WikimapiaObjectPage {
        return WikimapiaObjectPage(driver, config.baseUrl).open(objectPath)
    }
}
