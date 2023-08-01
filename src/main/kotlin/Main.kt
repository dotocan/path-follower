import com.github.ajalt.mordant.terminal.Terminal
import models.CharactersConfig
import models.PathFollowerConfig

val t = Terminal()

fun main(args: Array<String>) {
    val map = getMapFromTextFile("basic.txt")

    // Setup
    val pathFollower = PathFollower(
        map,
        config = PathFollowerConfig(
            shouldPrintEachStep = true,
            delayEachStepByMillis = 700,
            charactersConfig = CharactersConfig()
        )
    )

    // Logic
    val result = pathFollower.followPath()
    println(
        """
        -------------------------------------------
        Letters: ${result.collectedLetters}
        Path: ${result.collectedPath}
        -------------------------------------------
    """.trimIndent()
    )

}

