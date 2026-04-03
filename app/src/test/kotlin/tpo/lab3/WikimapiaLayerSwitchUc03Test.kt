package tpo.lab3

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaLayerSwitchUc03Test : BaseUiTest() {
    @Test
    fun `uc03 initial layer is map preview`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialSelectedLayer = homePage.selectedLayerPreviewClass()

        assertTrue(
            initialSelectedLayer.contains("wikimapia-imagetiles-map-preview"),
            "initial layer not selected",
        )
    }

    @Test
    fun `uc03 switching to satellite selects satellite preview`() {
        val homePage = openHomePageAt(55.75396, 37.620393)

        homePage.switchToSatelliteLayer()

        val updatedSelectedLayer = homePage.selectedLayerPreviewClass()

        assertTrue(
            updatedSelectedLayer.contains("google-satellite-preview"),
            "satellite layer not selected: '$updatedSelectedLayer'",
        )
    }

    @Test
    fun `uc03 switching to satellite changes map mode`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialMode = homePage.currentMapMode()

        homePage.switchToSatelliteLayer()

        val updatedMode = homePage.currentMapMode()

        assertTrue(
            updatedMode.isNotBlank() && updatedMode != initialMode,
            "map mode did not change: '$initialMode' -> '$updatedMode'",
        )
    }
}
