import models.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CharLocatorTest {

    @Test
    fun `findStartPointCoordinates should throw NoStartPointsException if no @ char is present`() {
        val map = getMapFromTextFile(error_missingStartCharacter.inputFilename)
        val charLocator = CharLocator(map, CharactersConfig())
        Assertions.assertThrows(error_missingStartCharacter.expectedErrorClass) { charLocator.findStartPointCoordinates() }
    }

    @Test
    fun `findStartPointCoordinates should throw MultipleStartPointsException if more than one @ chars are present`() {
        val map = getMapFromTextFile(error_multipleStarts1.inputFilename)
        val charLocator = CharLocator(map, CharactersConfig())
        Assertions.assertThrows(error_multipleStarts1.expectedErrorClass) { charLocator.findStartPointCoordinates() }
    }

    @Test
    fun `findEndPointCoordinates should throw NoEndPointsException if no x char is present`() {
        val map = getMapFromTextFile(error_missingEndCharacter.inputFilename)
        val charLocator = CharLocator(map, CharactersConfig())
        Assertions.assertThrows(error_missingEndCharacter.expectedErrorClass) { charLocator.findEndPointCoordinates() }
    }

    @Test
    fun `findEndPointCoordinates should throw MultipleEndPointsException if more than one x chars are present`() {
        val map = getMapFromTextFile("error-cases/multiple-ends.txt")
        val charLocator = CharLocator(map, CharactersConfig())
        Assertions.assertThrows(MultipleEndPointsException::class.java) { charLocator.findEndPointCoordinates() }
    }

    @Test
    fun `isCharAtCoordinates should return true if given character is at the the target coordinates`() {
        val map = getMapFromTextFile("basic.txt")
        val charLocator = CharLocator(map, CharactersConfig())

        val currentCharData = CharData('x', CharCoordinates(2, 0))
        val endPointCoordinates = charLocator.findEndPointCoordinates()

        assertTrue(charLocator.isCharAtCoordinates(currentCharData, endPointCoordinates))
    }

}