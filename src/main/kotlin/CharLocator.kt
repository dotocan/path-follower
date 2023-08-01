import models.*

class CharLocator(private val map: MutableMap<Int, CharArray>, charactersConfig: CharactersConfig) {
    private var startPointChar: Char = charactersConfig.startPointChar
    private var endPointChar: Char = charactersConfig.endPointChar

    fun findStartPointCoordinates(): CharCoordinates {
        var startPointCoordinates = CharCoordinates(0, 0)

        try {
            startPointCoordinates = findSingleCharCoordinates(startPointChar, map)
        } catch (e: Exception) {
            if (e is MultiplePointsException) throw MultipleStartPointsException()
            if (e is NoPointsException) throw NoStartPointsException()
        }

        return startPointCoordinates
    }

    fun findEndPointCoordinates(): CharCoordinates {
        var endPointCoordinates = CharCoordinates(0, 0)

        try {
            endPointCoordinates = findSingleCharCoordinates(endPointChar, map)
        } catch (e: Exception) {
            if (e is MultiplePointsException) throw MultipleEndPointsException()
            if (e is NoPointsException) throw NoEndPointsException()
        }

        return endPointCoordinates
    }

    fun isCharAtCoordinates(charData: CharData, targetCoordinates: CharCoordinates): Boolean {
        return charData.coordinates.rowIndex == targetCoordinates.rowIndex
                && charData.coordinates.colIndex == targetCoordinates.colIndex
    }

    fun getCharDataInDirection(currentCoordinates: CharCoordinates, targetDirection: DIRECTION): CharData {
        return when (targetDirection) {
            DIRECTION.UP -> getCharDataAtCoordinates(
                CharCoordinates(
                    currentCoordinates.rowIndex - 1,
                    currentCoordinates.colIndex
                )
            )

            DIRECTION.DOWN -> getCharDataAtCoordinates(
                CharCoordinates(
                    currentCoordinates.rowIndex + 1,
                    currentCoordinates.colIndex
                )
            )

            DIRECTION.LEFT -> getCharDataAtCoordinates(
                CharCoordinates(
                    currentCoordinates.rowIndex,
                    currentCoordinates.colIndex - 1
                )
            )

            DIRECTION.RIGHT -> getCharDataAtCoordinates(
                CharCoordinates(
                    currentCoordinates.rowIndex,
                    currentCoordinates.colIndex + 1
                )
            )

            DIRECTION.UNSET -> throw UnsetNextDirectionException()
        }
    }

    private fun getCharDataAtCoordinates(coordinates: CharCoordinates): CharData {
        var char: Char? = null

        if (isIndexWithinBoundsForCoordinates(map, coordinates)) {
            char = map[coordinates.rowIndex]?.get(coordinates.colIndex)
        }

        return CharData(char, coordinates)
    }

    private fun isIndexWithinBoundsForCoordinates(path: MutableMap<Int, CharArray>, coordinates: CharCoordinates): Boolean {
        return path.keys.contains(coordinates.rowIndex)
                && path[coordinates.rowIndex]!!.indices.contains(coordinates.colIndex)
    }

    private fun findSingleCharCoordinates(targetChar: Char, map: MutableMap<Int, CharArray>): CharCoordinates {
        var targetCharCount = 0
        val coordinates = CharCoordinates(0, 0)

        map.forEach { (key, value) ->
            value.forEach { character ->
                if (character == targetChar) {
                    targetCharCount++
                    coordinates.rowIndex = key
                    coordinates.colIndex = value.indexOf(targetChar)
                }
            }
        }

        if (targetCharCount > 1) throw MultiplePointsException()
        if (targetCharCount == 0) throw NoPointsException()

        return coordinates
    }
}