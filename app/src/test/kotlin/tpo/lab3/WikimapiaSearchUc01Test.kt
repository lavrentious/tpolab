package tpo.lab3

import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaSearchUc01Test : BaseUiTest() {
    @Test
    fun `uc01 search centers map on selected object`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialCoordinates = homePage.currentCoordinates()

        val searchQuery = "Kreml"
        val searchResults = homePage.search(searchQuery).waitUntilLoaded()

        assertTrue(
            searchResults.hasAnyResults(),
            "search returned no results",
        )

        val updatedCoordinates = searchResults.selectFirstResult()

        assertTrue(
            updatedCoordinates.differsFrom(initialCoordinates),
            "map did not move after search select",
        )
        assertTrue(
            URLDecoder.decode(driver.currentUrl, StandardCharsets.UTF_8)
                .contains("search=$searchQuery"),
            "search query missing in url",
        )
    }
}
