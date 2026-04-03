package tpo.lab3.config

data class TestConfig(
    val baseUrl: String,
    val browser: String,
    val headless: Boolean,
) {
    companion object {
        fun fromSystemProperties(): TestConfig =
            TestConfig(
                baseUrl = System.getProperty("baseUrl", "https://wikimapia.org"),
                browser = System.getProperty("browser", "chrome").lowercase(),
                headless = System.getProperty("headless", "true").toBooleanStrictOrNull() ?: true,
            )
    }
}
