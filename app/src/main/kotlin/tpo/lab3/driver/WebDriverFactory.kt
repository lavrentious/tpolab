package tpo.lab3.driver

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import tpo.lab3.config.TestConfig
import java.time.Duration

object WebDriverFactory {
    fun create(config: TestConfig): WebDriver {
        val driver = when (config.browser) {
            "chrome" -> createChromeDriver(config.headless)
            "firefox" -> createFirefoxDriver(config.headless)
            else -> error("unsupported browser '${config.browser}', use chrome or firefox")
        }

        driver.manage().timeouts().implicitlyWait(Duration.ZERO)
        driver.manage().window().maximize()
        return driver
    }

    private fun createChromeDriver(headless: Boolean): WebDriver {
        WebDriverManager.chromedriver().setup()

        val options = ChromeOptions()
        if (headless) {
            options.addArguments("--headless=new")
        }
        options.addArguments("--disable-dev-shm-usage")
        options.addArguments("--no-sandbox")
        options.addArguments("--window-size=1920,1080")

        return ChromeDriver(options)
    }

    private fun createFirefoxDriver(headless: Boolean): WebDriver {
        WebDriverManager.firefoxdriver().setup()

        val options = FirefoxOptions()
        if (headless) {
            options.addArguments("-headless")
        }
        options.addArguments("--width=1920")
        options.addArguments("--height=1080")

        return FirefoxDriver(options)
    }
}
