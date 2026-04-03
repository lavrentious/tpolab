package tpo.lab3

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import tpo.lab3.pages.WikimapiaHomePage
import java.util.stream.Stream

class WikimapiaLanguageUc04Test : BaseUiTest() {
    @ParameterizedTest(name = "switching to {0}")
    @MethodSource("languages")
    fun `uc04 switching interface language updates visible labels`(language: WikimapiaHomePage.Language) {
        val homePage = openHomePageAt(55.75396, 37.620393)
        val initialAddPlaceLabel = homePage.addPlaceLabel()

        homePage.switchToLanguage(language)

        val updatedAddPlaceLabel = homePage.addPlaceLabel()

        assertTrue(
            initialAddPlaceLabel.contains("Add place"),
            "initial label not english",
        )
        assertTrue(
            updatedAddPlaceLabel != initialAddPlaceLabel,
            "label did not change for ${language.code}",
        )
    }

    companion object {
        @JvmStatic
        fun languages(): Stream<WikimapiaHomePage.Language> = Stream.of(
            WikimapiaHomePage.Language(
                code = "ru",
                optionLabels = listOf("Russian", "Русский"),
            ),
            WikimapiaHomePage.Language(
                code = "de",
                optionLabels = listOf("German", "Deutsch"),
            ),
            WikimapiaHomePage.Language(
                code = "fr",
                optionLabels = listOf("French", "Français"),
            ),
        )
    }
}
