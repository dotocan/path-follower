import models.*

data class ExpectedPathFollowerResult(
    val inputFilename: String,
    val expectedLetters: String,
    val expectedPath: String,
)

data class ExpectedPathFollowerError<T>(
    val inputFilename: String,
    val expectedErrorClass: Class<T>? = null
)

val result_basic = ExpectedPathFollowerResult(
    "basic.txt",
    "ACB",
    "@---A---+|C|+---+|+-B-x",
)

val result_intersection = ExpectedPathFollowerResult(
    "intersection.txt",
    "ABCD",
    "@|A+---B--+|+--C-+|-||+---D--+|x"
)

val result_letterOnTurn = ExpectedPathFollowerResult(
    "letter-on-turn.txt",
    "ACB",
    "@---A---+|||C---+|+-B-x"
)

val result_letterOnSameLocationTwice = ExpectedPathFollowerResult(
    "letter-on-same-location-twice.txt",
    "GOONIES",
    "@-G-O-+|+-+|O||+-O-N-+|I|+-+|+-I-+|ES|x",
)

val result_compactSpace = ExpectedPathFollowerResult(
    "compact-space.txt",
    "BLAH",
    "@B+++B|+-L-+A+++A-+Hx",
)

val result_dontFollowPathAfterEnd = ExpectedPathFollowerResult(
    "path-after-end.txt",
    "AB",
    "@-A--+|+-B--x",
)

val error_missingStartCharacter = ExpectedPathFollowerError(
    "error-cases/missing-start-character.txt",
    expectedErrorClass = NoStartPointsException::class.java
)

val error_missingEndCharacter = ExpectedPathFollowerError(
    "error-cases/missing-end-character.txt",
    expectedErrorClass = NoEndPointsException::class.java
)

val error_multipleStarts1 = ExpectedPathFollowerError(
    "error-cases/multiple-starts-1.txt",
    expectedErrorClass = MultipleStartPointsException::class.java
)

val error_multipleStarts2 = ExpectedPathFollowerError(
    "error-cases/multiple-starts-2.txt",
    expectedErrorClass = MultipleStartPointsException::class.java
)

val error_multipleStarts3 = ExpectedPathFollowerError(
    "error-cases/multiple-starts-3.txt",
    expectedErrorClass = MultipleStartPointsException::class.java
)

/*
// this example throws MultipleEndPointsException so I removed one endpoint in the modified version below
val error_forkInPath = ExpectedPathFollowerError(
    "error-cases/fork-in-path.txt",
    expectedErrorClass = ForkInPathException::class.java
)
*/

val error_forkInPathModified = ExpectedPathFollowerError(
    "special-test-cases/fork-in-path-modified.txt",
    expectedErrorClass = ForkInPathException::class.java
)

val error_brokenPath = ExpectedPathFollowerError(
    "error-cases/broken-path.txt",
    expectedErrorClass = BrokenPathException::class.java
)

/*
// original example throws MultipleEndPointsException so I removed one endpoint (check modified version below)
val error_multipleStartingPaths = ExpectedPathFollowerError(
    "error-cases/multiple-starting-paths.txt",
    expectedErrorClass = MultipleStartingPathsException::class.java
)
 */


val error_multipleStartingPathsModified = ExpectedPathFollowerError(
    "special-test-cases/multiple-starting-paths-modified.txt",
    expectedErrorClass = MultipleStartingPathsException::class.java
)

val error_fakeTurn = ExpectedPathFollowerError(
    "error-cases/fake-turn.txt",
    expectedErrorClass = FakeTurnException::class.java
)
