package tpo.lab3

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaStandaloneObjectPageTest : BaseUiTest() {
    @Test
    fun `standalone object page shows title`() {
        val objectPage = openUnknownSoldierPage()

        assertTrue(
            objectPage.title().contains("Tomb of the Unknown Soldier"),
            "object title mismatch",
        )
    }

    @Test
    fun `standalone object page shows description`() {
        val objectPage = openUnknownSoldierPage()

        assertTrue(
            objectPage.description().contains("war memorial"),
            "object description mismatch",
        )
    }

    @Test
    fun `standalone object page shows photos`() {
        val objectPage = openUnknownSoldierPage()

        assertTrue(
            objectPage.photoCount() > 0,
            "object has no photos",
        )
    }

    @Test
    fun `standalone object page shows categories`() {
        val objectPage = openUnknownSoldierPage()

        assertTrue(
            objectPage.categories().contains("grave") &&
                objectPage.categories().contains("war memorial"),
            "object categories mismatch",
        )
    }

    @Test
    fun `standalone object page content remains after closing comment dialog`() {
        val objectPage = openUnknownSoldierPage()

        if (objectPage.isCommentDialogVisible()) {
            objectPage.closeCommentDialog()

            assertTrue(
                objectPage.title().contains("Tomb of the Unknown Soldier"),
                "object content changed after dialog close",
            )
        }
    }

    private fun openUnknownSoldierPage() = openObjectPage("/42427/Tomb-of-the-Unknown-Soldier")
}
