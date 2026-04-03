package tpo.lab3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaZoomUc05Test : BaseUiTest() {
    @Test
    fun `uc05 clicking zoom in increases map zoom level`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialCoordinates = homePage.currentCoordinates()
        val initialZoomLevel = homePage.currentZoomLevel()

        homePage.zoomIn()

        val updatedCoordinates = homePage.currentCoordinates()
        val updatedZoomLevel = homePage.currentZoomLevel()
        val updatedUrl = driver.currentUrl.orEmpty()

        assertEquals(
            initialZoomLevel + 1,
            updatedZoomLevel,
            "zoom did not increase by 1",
        )
        assertTrue(
            !updatedCoordinates.differsFrom(initialCoordinates),
            "map center changed after zoom",
        )
        assertTrue(
            updatedUrl.contains("z=$updatedZoomLevel"),
            "zoom missing in url: '$updatedUrl'",
        )
    }
}
