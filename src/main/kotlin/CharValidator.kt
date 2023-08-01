import models.CharactersConfig
import models.DIRECTION

class CharValidator(charactersConfig: CharactersConfig) {
    private var endPointChar = charactersConfig.endPointChar
    private var intersectionChar = charactersConfig.intersectionChar
    private var horizontalPathChar = charactersConfig.horizontalPathChar
    private var verticalPathChar = charactersConfig.verticalPathChar

    fun isCharacterAPath(character: Char?): Boolean {
        if (character == null) return false
        return character == horizontalPathChar || character == verticalPathChar
    }

    fun isCharacterAnIntersection(character: Char?): Boolean {
        if (character == null) return false
        return character == intersectionChar
    }

    fun isCharacterATurn(character: Char?): Boolean {
        if (character == null) return false
        return character.isUpperCase() || isCharacterAnIntersection(character)
    }

    fun isCharacterEnd(character: Char?): Boolean {
        if (character == null) return false
        return character == endPointChar
    }

    fun isValidNextCharacter(character: Char?): Boolean {
        if (character == null) return false
        return isCharacterAPath(character)
                || isCharacterATurn(character)
                || isCharacterEnd(character)
    }

    fun doesCharacterMakeSenseInDirection(
        currentCharacter: Char?,
        targetCharacter: Char?,
        direction: DIRECTION
    ): Boolean {
        if (currentCharacter == intersectionChar) {
            if (direction == DIRECTION.UP || direction == DIRECTION.DOWN) {
                return targetCharacter != horizontalPathChar
            }

            if (direction == DIRECTION.LEFT || direction == DIRECTION.RIGHT) {
                return targetCharacter != verticalPathChar
            }
        }

        return true
    }
}