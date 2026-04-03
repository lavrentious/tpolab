package tpo.lab3

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaStandaloneObjectPageTest : BaseUiTest() {
    @Test
    fun `standalone object page shows description photos and categories`() {
        val objectPage = openObjectPage("/42427/Tomb-of-the-Unknown-Soldier")

        assertTrue(
            objectPage.title().contains("Tomb of the Unknown Soldier"),
            "object title mismatch",
        )
        assertTrue(
            objectPage.description().contains("war memorial"),
            "object description mismatch",
        )
        assertTrue(
            objectPage.photoCount() > 0,
            "object has no photos",
        )
        assertTrue(
            objectPage.categories().contains("grave") &&
                objectPage.categories().contains("war memorial"),
            "object categories mismatch",
        )
        if (objectPage.isCommentDialogVisible()) {
            objectPage.closeCommentDialog()

            assertTrue(
                objectPage.title().contains("Tomb of the Unknown Soldier"),
                "object content changed after dialog close",
            )
        }
    }
}
