package models

data class SurroundingChars(
    val up: CharData,
    val down: CharData,
    val left: CharData,
    val right: CharData
)
