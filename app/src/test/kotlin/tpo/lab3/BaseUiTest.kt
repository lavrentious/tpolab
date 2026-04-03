package tpo.lab3

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.WebDriver
import tpo.lab3.config.TestConfig
import tpo.lab3.driver.WebDriverFactory
import tpo.lab3.pages.WikimapiaHomePage

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
}
