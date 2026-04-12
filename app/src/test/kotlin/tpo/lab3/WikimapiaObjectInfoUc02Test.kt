package tpo.lab3

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaObjectInfoUc02Test : BaseUiTest() {
    @Test
    fun `uc02 clicking map object opens infobox`() {
        val mapPage = openTrinityTowerInfoBox()

        assertTrue(
            mapPage.isInfoBoxOpen(),
            "infobox did not open",
        )
    }

    @Test
    fun `uc02 infobox shows object title`() {
        val mapPage = openTrinityTowerInfoBox()

        assertTrue(
            mapPage.infoBoxTitle().contains("Trinity Tower"),
            "infobox title mismatch: '${mapPage.infoBoxTitle()}'",
        )
    }

    @Test
    fun `uc02 infobox shows object description`() {
        val mapPage = openTrinityTowerInfoBox()

        assertTrue(
            mapPage.infoBoxDescription().contains("Moscow Kremlin"),
            "infobox description mismatch: '${mapPage.infoBoxDescription()}'",
        )
    }

    @Test
    fun `uc02 infobox shows photos`() {
        val mapPage = openTrinityTowerInfoBox()

        assertTrue(
            mapPage.infoBoxPhotoCount() > 0,
            "infobox has no photos",
        )
    }

    @Test
    fun `uc02 infobox shows categories`() {
        val mapPage = openTrinityTowerInfoBox()

        assertTrue(
            mapPage.infoBoxCategories().contains("tower") &&
                mapPage.infoBoxCategories().contains("listed building / architectural heritage"),
            "infobox categories mismatch: '${mapPage.infoBoxCategories()}'",
        )
    }

    @Test
    fun `uc02 infobox can be closed`() {
        val mapPage = openTrinityTowerInfoBox()

        mapPage.closeInfoBox()

        assertFalse(
            mapPage.isInfoBoxOpen(),
            "infobox did not close",
        )
    }

    private fun openTrinityTowerInfoBox() = openMapObjectInfoPageAt(55.752024, 37.617499)
        .openInfoBox("Trinity Tower", "/249/Trinity-Tower")
}
