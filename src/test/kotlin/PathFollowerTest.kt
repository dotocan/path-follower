import models.CharactersConfig
import models.PathFollowerConfig
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PathFollowerTest {
    private val defaultConfig = PathFollowerConfig(
        // 3rd party mordant Terminal doesn't seem to work when running tests so pretty printing is turned off
        shouldPrintEachStep = false,
        charactersConfig = CharactersConfig()
    )

    @Test
    fun `followPath should return expected letters and paths for basic example`() {
        val map = getMapFromTextFile(result_basic.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        val result = pathFollower.followPath()

        assertEquals(result_basic.expectedPath, result.collectedPath)
        assertEquals(result_basic.expectedLetters, result.collectedLetters)
    }

    @Test
    fun `followPath should return expected letters and paths and go straight trough intersection`() {
        val map = getMapFromTextFile(result_intersection.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        val result = pathFollower.followPath()

        assertEquals(result_intersection.expectedPath, result.collectedPath)
        assertEquals(result_intersection.expectedLetters, result.collectedLetters)
    }

    @Test
    fun `followPath should return expected letters and paths when there is a letter on turn`() {
        val map = getMapFromTextFile(result_letterOnTurn.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        val result = pathFollower.followPath()

        assertEquals(result_letterOnTurn.expectedPath, result.collectedPath)
        assertEquals(result_letterOnTurn.expectedLetters, result.collectedLetters)
    }

    @Test
    fun `followPath should return expected letters and paths and avoid collecting the same letter twice`() {
        val map = getMapFromTextFile(result_letterOnSameLocationTwice.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        val result = pathFollower.followPath()

        assertEquals(result_letterOnSameLocationTwice.expectedPath, result.collectedPath)
        assertEquals(result_letterOnSameLocationTwice.expectedLetters, result.collectedLetters)
    }

    @Test
    fun `followPath should return expected letters and paths and keep direction in compact space`() {
        val map = getMapFromTextFile(result_compactSpace.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        val result = pathFollower.followPath()

        assertEquals(result_compactSpace.expectedPath, result.collectedPath)
        assertEquals(result_compactSpace.expectedLetters, result.collectedLetters)
    }

    @Test
    fun `followPath should return expected letter, but ignore path after reaching end character`() {
        val map = getMapFromTextFile(result_dontFollowPathAfterEnd.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        val result = pathFollower.followPath()

        assertEquals(result_dontFollowPathAfterEnd.expectedPath, result.collectedPath)
        assertEquals(result_dontFollowPathAfterEnd.expectedLetters, result.collectedLetters)
    }

    @Test
    fun `followPath should throw error when no start character is found`() {
        val map = getMapFromTextFile(error_missingStartCharacter.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_missingStartCharacter.expectedErrorClass) {
            pathFollower.followPath()
        }
    }

    @Test
    fun `followPath should throw error when no end character is found`() {
        val map = getMapFromTextFile(error_missingEndCharacter.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_missingEndCharacter.expectedErrorClass) {
            pathFollower.followPath()
        }
    }

    @Test
    fun `followPath should throw error when there are multiple starts, example 1`() {
        val map = getMapFromTextFile(error_multipleStarts1.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_multipleStarts1.expectedErrorClass) {
            pathFollower.followPath()
        }
    }

    @Test
    fun `followPath should throw error when there are multiple starts, example 2`() {
        val map = getMapFromTextFile(error_multipleStarts2.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_multipleStarts2.expectedErrorClass) {
            pathFollower.followPath()
        }
    }

    @Test
    fun `followPath should throw error when there are multiple starts, example 3`() {
        val map = getMapFromTextFile(error_multipleStarts3.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_multipleStarts3.expectedErrorClass) {
            pathFollower.followPath()
        }
    }

    /*
    // this example throws MultipleEndPointsException so I removed one endpoint in the modified version below
    @Test
    fun `followPath should throw error when there is a fork in path`() {
        val map = getMapFromTextFile(error_forkInPath.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_forkInPath.expectedErrorClass) {
            pathFollower.followPath()
        }
    }*/

    @Test
    fun `followPath should throw error when there is a fork in path (modified)`() {
        val map = getMapFromTextFile(error_forkInPathModified.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_forkInPathModified.expectedErrorClass) {
            pathFollower.followPath()
        }
    }

    @Test
    fun `followPath should throw error when the path is broken`() {
        val map = getMapFromTextFile(error_brokenPath.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_brokenPath.expectedErrorClass) {
            pathFollower.followPath()
        }
    }

    /*
    // this example throws MultipleEndPointsException so I removed one endpoint in the modified version below
    @Test
    fun `followPath should throw error when there are multiple starting paths`() {
        val map = getMapFromTextFile(error_multipleStartingPaths.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_multipleStartingPaths.expectedErrorClass) {
            pathFollower.followPath()
        }
    }*/

    @Test
    fun `followPath should throw error when there are multiple starting paths (modified)`() {
        val map = getMapFromTextFile(error_multipleStartingPathsModified.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_multipleStartingPathsModified.expectedErrorClass) {
            pathFollower.followPath()
        }
    }


    @Test
    fun `followPath should throw error when there is a fake turn`() {
        val map = getMapFromTextFile(error_fakeTurn.inputFilename)
        val pathFollower = PathFollower(map, defaultConfig)

        Assertions.assertThrows(error_fakeTurn.expectedErrorClass) {
            pathFollower.followPath()
        }
    }


    /*@Test
    fun `getCoordinatesInDirection should return correct up coordinate`() {
        val map = getMapFromTextFile("special-test-cases/coordinates-direction.txt")
        val pathFollower = PathFollower(map, defaultConfig)

        val currentCoordinates = CharCoordinates(1, 1)
        val expectedCoordinates = CharCoordinates(0, 1)
        val resultCoordinates = pathFollower.getCoordinatesInDirection(currentCoordinates, DIRECTION.UP)

        assertEquals(expectedCoordinates.rowIndex, resultCoordinates.rowIndex)
        assertEquals(expectedCoordinates.colIndex, resultCoordinates.colIndex)
    }

    @Test
    fun `getCoordinatesInDirection should return correct down coordinate`() {
        val map = getMapFromTextFile("special-test-cases/coordinates-direction.txt")
        val pathFollower = PathFollower(map, defaultConfig)

        val currentCoordinates = CharCoordinates(1, 1)
        val expectedCoordinates = CharCoordinates(2, 1)
        val resultCoordinates = pathFollower.getCoordinatesInDirection(currentCoordinates, DIRECTION.DOWN)

        assertEquals(expectedCoordinates.rowIndex, resultCoordinates.rowIndex)
        assertEquals(expectedCoordinates.colIndex, resultCoordinates.colIndex)
    }

    @Test
    fun `getCoordinatesInDirection should return correct left coordinate`() {
        val map = getMapFromTextFile("special-test-cases/coordinates-direction.txt")
        val pathFollower = PathFollower(map, defaultConfig)

        val currentCoordinates = CharCoordinates(1, 1)
        val expectedCoordinates = CharCoordinates(1, 0)
        val resultCoordinates = pathFollower.getCoordinatesInDirection(currentCoordinates, DIRECTION.LEFT)

        assertEquals(expectedCoordinates.rowIndex, resultCoordinates.rowIndex)
        assertEquals(expectedCoordinates.colIndex, resultCoordinates.colIndex)
    }

    @Test
    fun `getCoordinatesInDirection should return correct right coordinate`() {
        val map = getMapFromTextFile("special-test-cases/coordinates-direction.txt")
        val pathFollower = PathFollower(map, defaultConfig)

        val currentCoordinates = CharCoordinates(1, 1)
        val expectedCoordinates = CharCoordinates(1, 2)
        val resultCoordinates = pathFollower.getCoordinatesInDirection(currentCoordinates, DIRECTION.RIGHT)

        assertEquals(expectedCoordinates.rowIndex, resultCoordinates.rowIndex)
        assertEquals(expectedCoordinates.colIndex, resultCoordinates.colIndex)
    }*/

}