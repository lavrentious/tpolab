package tpo.lab3

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaLayerSwitchUc03Test : BaseUiTest() {
    @Test
    fun `uc03 switching to satellite changes active map layer`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialMode = homePage.currentMapMode()
        val initialSelectedLayer = homePage.selectedLayerPreviewClass()

        homePage.switchToSatelliteLayer()

        val updatedMode = homePage.currentMapMode()
        val updatedSelectedLayer = homePage.selectedLayerPreviewClass()

        assertTrue(
            initialSelectedLayer.contains("wikimapia-imagetiles-map-preview"),
            "Expected the map layer to be selected before switching layers.",
        )
        assertTrue(
            updatedSelectedLayer.contains("google-satellite-preview"),
            "Expected the satellite layer to become the selected layer, but got '$updatedSelectedLayer'.",
        )
        assertTrue(
            updatedMode.isNotBlank() && updatedMode != initialMode,
            "Expected the URL map mode to change after switching to satellite, but got '$initialMode' -> '$updatedMode'.",
        )
    }
}
