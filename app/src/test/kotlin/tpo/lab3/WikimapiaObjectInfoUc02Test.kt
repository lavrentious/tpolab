package tpo.lab3

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaObjectInfoUc02Test : BaseUiTest() {
    @Test
    fun `uc02 clicking map object opens infobox with details and can be closed`() {
        val mapPage = openMapObjectInfoPageAt(55.752024, 37.617499)
            .openInfoBox("Trinity Tower", "/249/Trinity-Tower")

        assertTrue(
            mapPage.isInfoBoxOpen(),
            "infobox did not open",
        )
        assertTrue(
            mapPage.infoBoxTitle().contains("Trinity Tower"),
            "infobox title mismatch: '${mapPage.infoBoxTitle()}'",
        )
        assertTrue(
            mapPage.infoBoxDescription().contains("Moscow Kremlin"),
            "infobox description mismatch: '${mapPage.infoBoxDescription()}'",
        )
        assertTrue(
            mapPage.infoBoxPhotoCount() > 0,
            "infobox has no photos",
        )
        assertTrue(
            mapPage.infoBoxCategories().contains("tower") &&
                mapPage.infoBoxCategories().contains("listed building / architectural heritage"),
            "infobox categories mismatch: '${mapPage.infoBoxCategories()}'",
        )

        mapPage.closeInfoBox()

        assertTrue(
            !mapPage.isInfoBoxOpen(),
            "infobox did not close",
        )
    }
}
