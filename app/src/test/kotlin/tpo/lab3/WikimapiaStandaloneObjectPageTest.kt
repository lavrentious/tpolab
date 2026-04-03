package tpo.lab3

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaStandaloneObjectPageTest : BaseUiTest() {
    @Test
    fun `standalone object page shows description photos and categories`() {
        val objectPage = openObjectPage("/42427/Tomb-of-the-Unknown-Soldier")

        assertTrue(
            objectPage.title().contains("Tomb of the Unknown Soldier"),
            "Expected the object page to display the selected object's title.",
        )
        assertTrue(
            objectPage.description().contains("war memorial"),
            "Expected the object page description to include the object's details.",
        )
        assertTrue(
            objectPage.photoCount() > 0,
            "Expected the object page to display at least one photo.",
        )
        assertTrue(
            objectPage.categories().contains("grave") &&
                objectPage.categories().contains("war memorial"),
            "Expected the object page to list the main categories.",
        )
        if (objectPage.isCommentDialogVisible()) {
            objectPage.closeCommentDialog()

            assertTrue(
                objectPage.title().contains("Tomb of the Unknown Soldier"),
                "Expected the object page content to remain available after closing the dialog overlay.",
            )
        }
    }
}
