package tpo.lab3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaZoomUc05Test : BaseUiTest() {
    @Test
    fun `uc05 clicking zoom in increases map zoom level by one`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialZoomLevel = homePage.currentZoomLevel()

        homePage.zoomIn()

        val updatedZoomLevel = homePage.currentZoomLevel()

        assertEquals(
            initialZoomLevel + 1,
            updatedZoomLevel,
            "zoom did not increase by 1",
        )
    }

    @Test
    fun `uc05 clicking zoom in keeps map center unchanged`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialCoordinates = homePage.currentCoordinates()

        homePage.zoomIn()

        val updatedCoordinates = homePage.currentCoordinates()

        assertTrue(
            !updatedCoordinates.differsFrom(initialCoordinates),
            "map center changed after zoom",
        )
    }

    @Test
    fun `uc05 clicking zoom in updates zoom in url`() {
        val homePage = openHomePageAt(55.75396, 37.620393)

        homePage.zoomIn()

        val updatedZoomLevel = homePage.currentZoomLevel()
        val updatedUrl = driver.currentUrl.orEmpty()

        assertTrue(
            updatedUrl.contains("z=$updatedZoomLevel"),
            "zoom missing in url: '$updatedUrl'",
        )
    }
}
