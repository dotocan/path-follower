package models

data class PathFollowerConfig(
    val shouldPrintEachStep: Boolean = false,
    val delayEachStepByMillis: Long = 0,
    val charactersConfig: CharactersConfig
)