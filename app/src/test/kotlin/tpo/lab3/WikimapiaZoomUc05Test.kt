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
            "Expected clicking the zoom in control to increase the zoom level by one.",
        )
        assertTrue(
            !updatedCoordinates.differsFrom(initialCoordinates),
            "Expected zooming in through the control to keep the map centered on the same location.",
        )
        assertTrue(
            updatedUrl.contains("z=$updatedZoomLevel"),
            "Expected the updated zoom level to be reflected in the URL, but got '$updatedUrl'.",
        )
    }
}
