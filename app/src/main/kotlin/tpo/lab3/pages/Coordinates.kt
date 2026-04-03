package tpo.lab3.pages

import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.math.abs

data class Coordinates(
    val latitude: Double,
    val longitude: Double,
) {
    fun differsFrom(other: Coordinates, threshold: Double = 0.0001): Boolean =
        abs(latitude - other.latitude) > threshold || abs(longitude - other.longitude) > threshold

    companion object {
        fun fromUrl(url: String): Coordinates {
            val fragment = url.substringAfter('#', missingDelimiterValue = "")
            val params = parseFragmentParameters(fragment)

            val latitude = parseCoordinate(params, "lat", "Latitude", url)
            val longitude = parseCoordinate(params, "lon", "Longitude", url)

            return Coordinates(latitude, longitude)
        }

        private fun parseFragmentParameters(fragment: String): Map<String, String> {
            val parameterSection = fragment.substringBefore('?')
            val params = mutableMapOf<String, String>()

            for (item in parameterSection.split('&')) {
                val parts = item.split('=', limit = 2)
                if (parts.size != 2) {
                    continue
                }

                val name = parts[0]
                val value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                params[name] = value
            }

            return params
        }

        private fun parseCoordinate(
            params: Map<String, String>,
            key: String,
            fieldName: String,
            url: String,
        ): Double {
            val value = params[key] ?: error("$fieldName not found in URL fragment: $url")
            return value.toDoubleOrNull() ?: error("$fieldName is not a valid number in URL fragment: $url")
        }
    }
}
