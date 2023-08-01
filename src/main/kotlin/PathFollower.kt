import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.table.table
import models.*

class PathFollower(
    private val map: MutableMap<Int, CharArray>,
    private val config: PathFollowerConfig,
    private val charLocator: CharLocator = CharLocator(map, config.charactersConfig),
    private val charValidator: CharValidator = CharValidator(config.charactersConfig),
) {
    private var startPointChar: Char = config.charactersConfig.startPointChar
    private var endPointChar: Char = config.charactersConfig.endPointChar

    private lateinit var currentCharData: CharData
    private var currentDirection: DIRECTION = DIRECTION.UNSET

    fun followPath(): PathFollowerResult {
        val collectedPath: ArrayList<Char> = arrayListOf()
        val collectedLettersMap: MutableMap<String, Char> = mutableMapOf()

        val startpointCoordinates = charLocator.findStartPointCoordinates()
        val endPointCoordinates = charLocator.findEndPointCoordinates()

        currentCharData =
            CharData(config.charactersConfig.startPointChar, startpointCoordinates)

        while (!charLocator.isCharAtCoordinates(currentCharData, endPointCoordinates)) {

            if (config.shouldPrintEachStep) printCurrentState()

            if (currentCharData.character == startPointChar) {
                collectedPath.add(startPointChar)
            }

            val nextCharData = findNextCharData()
            if (nextCharData.character != null) {
                currentCharData = CharData(nextCharData.character, nextCharData.coordinates)
                collectedPath.add(nextCharData.character)

                if (nextCharData.character.isUpperCase() && nextCharData.character != endPointChar) {
                    // Create lettersMap key out of coordinates, this way it will only collect a letter
                    // from the same location once and ignore it the second time
                    val coordinatesString =
                        "${nextCharData.coordinates.rowIndex}${nextCharData.coordinates.colIndex}"

                    collectedLettersMap[coordinatesString] = nextCharData.character
                }
            }

            if (config.shouldPrintEachStep)
                printCurrentResult(collectedPath, collectedLettersMap.values)

            if (config.delayEachStepByMillis > 0) {
                Thread.sleep(config.delayEachStepByMillis)
            }
        }

        return PathFollowerResult(
            collectedLettersMap.values.joinToString(""),
            collectedPath.joinToString("")
        )
    }

    private fun findNextCharData(): CharData {
        val surroundingChars = SurroundingChars(
            up = charLocator.getCharDataInDirection(currentCharData.coordinates, DIRECTION.UP),
            down = charLocator.getCharDataInDirection(currentCharData.coordinates, DIRECTION.DOWN),
            left = charLocator.getCharDataInDirection(currentCharData.coordinates, DIRECTION.LEFT),
            right = charLocator.getCharDataInDirection(currentCharData.coordinates, DIRECTION.RIGHT)
        )

        val nextDirection = determineNextDirection(surroundingChars)

        if (config.shouldPrintEachStep)
            printDirectionDecisionStatus(surroundingChars, nextDirection)

        when (nextDirection) {
            DIRECTION.UP -> {
                currentDirection = DIRECTION.UP
                return surroundingChars.up
            }

            DIRECTION.DOWN -> {
                currentDirection = DIRECTION.DOWN
                return surroundingChars.down
            }

            DIRECTION.LEFT -> {
                currentDirection = DIRECTION.LEFT
                return surroundingChars.left
            }

            DIRECTION.RIGHT -> {
                currentDirection = DIRECTION.RIGHT
                return surroundingChars.right
            }

            DIRECTION.UNSET -> {
                throw UnsetNextDirectionException()
            }
        }
    }

    fun determineNextDirection(surroundingChars: SurroundingChars): DIRECTION {
        var nextDirection: DIRECTION = DIRECTION.UNSET
        val potentialNextDirections = findAllPotentialDirections(surroundingChars)

        if (potentialNextDirections.size < 1) {
            throw BrokenPathException()
        }

        if (potentialNextDirections.size == 1) {
            nextDirection = potentialNextDirections.first()

            if (charValidator.isCharacterAnIntersection(currentCharData.character)
                && nextDirection == currentDirection) {
                throw FakeTurnException()
            }
        }

        if (potentialNextDirections.size > 1) {
            // Fail immediately if starting character has several possible directions to go
            if (currentCharData.character == startPointChar) {
                throw MultipleStartingPathsException()
            }

            // If current character is a normal path, the only way to continue is in the same direction
            if (charValidator.isCharacterAPath(currentCharData.character)) {
                nextDirection = currentDirection
            }

            // Current character is a turn character (intersection or a letter)
            // and there are multiple possible directions. The only valid option
            // in this case is to continue in the same direction. If that is not
            // possible, we arrived at a fork in path and should fail
            if (charValidator.isCharacterATurn(currentCharData.character)) {
                var canContinueInTheSameDirection = false

                when (currentDirection) {
                    DIRECTION.UNSET -> throw UnsetNextDirectionException()
                    DIRECTION.UP -> {
                        if (charValidator.isValidNextCharacter(surroundingChars.up.character)) {
                            canContinueInTheSameDirection = true
                        }
                    }

                    DIRECTION.DOWN -> {
                        if (charValidator.isValidNextCharacter(surroundingChars.down.character)) {
                            canContinueInTheSameDirection = true
                        }
                    }

                    DIRECTION.LEFT -> {
                        if (charValidator.isValidNextCharacter(surroundingChars.left.character)) {
                            canContinueInTheSameDirection = true
                        }
                    }

                    DIRECTION.RIGHT -> {
                        if (charValidator.isValidNextCharacter(surroundingChars.right.character)) {
                            canContinueInTheSameDirection = true
                        }
                    }
                }

                if (canContinueInTheSameDirection) {
                    nextDirection = currentDirection
                } else {
                    throw ForkInPathException()
                }
            }
        }

        return nextDirection
    }

    fun findAllPotentialDirections(surroundingChars: SurroundingChars): MutableList<DIRECTION> {
        val potentialNextDirections: MutableList<DIRECTION> = mutableListOf()

        if (isNotPreviousDirection(DIRECTION.UP)
            && charValidator.isValidNextCharacter(surroundingChars.up.character)
            && charValidator.doesCharacterMakeSenseInDirection(
                currentCharData.character,
                surroundingChars.up.character,
                DIRECTION.UP
            )
        ) {
            potentialNextDirections.add(DIRECTION.UP)
        }

        if (isNotPreviousDirection(DIRECTION.DOWN)
            && charValidator.isValidNextCharacter(surroundingChars.down.character)
            && charValidator.doesCharacterMakeSenseInDirection(
                currentCharData.character,
                surroundingChars.down.character,
                DIRECTION.DOWN
            )
        ) {
            potentialNextDirections.add(DIRECTION.DOWN)
        }

        if (isNotPreviousDirection(DIRECTION.LEFT)
            && charValidator.isValidNextCharacter(surroundingChars.left.character)
            && charValidator.doesCharacterMakeSenseInDirection(
                currentCharData.character,
                surroundingChars.left.character,
                DIRECTION.LEFT
            )
        ) {
            potentialNextDirections.add(DIRECTION.LEFT)
        }

        if (isNotPreviousDirection(DIRECTION.RIGHT)
            && charValidator.isValidNextCharacter(surroundingChars.right.character)
            && charValidator.doesCharacterMakeSenseInDirection(
                currentCharData.character,
                surroundingChars.right.character,
                DIRECTION.RIGHT
            )
        ) {
            potentialNextDirections.add(DIRECTION.RIGHT)
        }

        return potentialNextDirections
    }


    /*
     If we already have a direction set, we don't want to check the direction we came
     from.
     Example:
     @---+
         |
         |
     We follow the path from the start and arrive at the intersection. If we check for all four available directions
     here, the program will find both LEFT and DOWN as possible next directions.
     We need to skip checking the direction from which we came.
    */
    fun isNotPreviousDirection(directionToCheck: DIRECTION): Boolean {
        // At the start, check all four directions since we don't have a previous one
        if (currentDirection == DIRECTION.UNSET) return true

        val oppositeOfCurrentDirection = getOppositeDirection(currentDirection)
        return oppositeOfCurrentDirection != directionToCheck
    }

    fun getOppositeDirection(direction: DIRECTION): DIRECTION {
        return when (direction) {
            DIRECTION.UNSET -> DIRECTION.UNSET // unset doesn't have an opposite
            DIRECTION.UP -> DIRECTION.DOWN
            DIRECTION.DOWN -> DIRECTION.UP
            DIRECTION.LEFT -> DIRECTION.RIGHT
            DIRECTION.RIGHT -> DIRECTION.LEFT
        }
    }

    private fun printDirectionDecisionStatus(surroundingChars: SurroundingChars, nextDirection: DIRECTION) {
        t.println(table {
            header { row("Up", "Down", "Left", "Right", "Next direction") }
            body {
                row(
                    surroundingChars.up.character,
                    surroundingChars.down.character,
                    surroundingChars.left.character,
                    surroundingChars.right.character,
                    nextDirection
                )
            }
        })
    }

    private fun printCurrentResult(
        collectedPath: ArrayList<Char>,
        collectedLetters: MutableCollection<Char>
    ) {
        println("Path: ${collectedPath.joinToString("")}")
        println("Letters: ${collectedLetters.joinToString("")}")
    }

    private fun printCurrentState() {
        println()
        println("################################################")
        println()

        printMap(map, currentCharData)
    }

    private fun printMap(map: MutableMap<Int, CharArray>, currentCharData: CharData) {
        map.forEach { (key, value) ->
            value.forEachIndexed { index, character ->
                if (currentCharData.coordinates.rowIndex == key && currentCharData.coordinates.colIndex == index) {
                    t.print(TextColors.red(character.toString()))
                } else {
                    t.print(character)
                }
            }
            println()
        }
    }
}