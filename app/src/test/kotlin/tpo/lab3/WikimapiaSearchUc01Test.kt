package tpo.lab3

import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaSearchUc01Test : BaseUiTest() {
    @Test
    fun `uc01 search returns at least one result`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val searchResults = search(homePage)

        assertTrue(
            searchResults.hasAnyResults(),
            "search returned no results",
        )
    }

    @Test
    fun `uc01 selecting search result moves map center`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialCoordinates = homePage.currentCoordinates()
        val searchResults = search(homePage)

        assertTrue(searchResults.hasAnyResults(), "search returned no results")

        val updatedCoordinates = searchResults.selectFirstResult()

        assertTrue(
            updatedCoordinates.differsFrom(initialCoordinates),
            "map did not move after search select",
        )
    }

    @Test
    fun `uc01 selecting search result keeps search query in url`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val searchResults = search(homePage)

        assertTrue(searchResults.hasAnyResults(), "search returned no results")

        searchResults.selectFirstResult()

        assertTrue(
            URLDecoder.decode(driver.currentUrl, StandardCharsets.UTF_8).contains("search=$SEARCH_QUERY"),
            "search query missing in url",
        )
    }

    private fun search(homePage: tpo.lab3.pages.WikimapiaHomePage) =
        homePage.search(SEARCH_QUERY).waitUntilLoaded()

    companion object {
        private const val SEARCH_QUERY = "Kreml"
    }
}
