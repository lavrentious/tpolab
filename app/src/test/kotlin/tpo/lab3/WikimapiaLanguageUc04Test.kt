package tpo.lab3

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class WikimapiaLanguageUc04Test : BaseUiTest() {
    @Test
    fun `uc04 switching interface language updates visible labels`() {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialAddPlaceLabel = homePage.addPlaceLabel()

        homePage.switchToRussianLanguage()

        val updatedAddPlaceLabel = homePage.addPlaceLabel()

        assertTrue(
            initialAddPlaceLabel.contains("Add place"),
            "initial label not english",
        )
        assertTrue(
            updatedAddPlaceLabel.contains("Добавить"),
            "label not russian",
        )
    }
}
