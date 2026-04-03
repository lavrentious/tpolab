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
            "Expected clicking an object contour on the map to open the info box.",
        )
        assertTrue(
            mapPage.infoBoxTitle().contains("Trinity Tower"),
            "Expected the info box to display the selected object's title.",
        )
        assertTrue(
            mapPage.infoBoxDescription().contains("Moscow Kremlin"),
            "Expected the info box description to include object details.",
        )
        assertTrue(
            mapPage.infoBoxPhotoCount() > 0,
            "Expected the info box to display at least one photo.",
        )
        assertTrue(
            mapPage.infoBoxCategories().contains("tower") &&
                mapPage.infoBoxCategories().contains("listed building / architectural heritage"),
            "Expected the info box to list the main categories.",
        )

        mapPage.closeInfoBox()

        assertTrue(
            !mapPage.isInfoBoxOpen(),
            "Expected the info box to close and return the user to the map.",
        )
    }
}
