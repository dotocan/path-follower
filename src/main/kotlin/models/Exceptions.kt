package models

class MultiplePointsException(message: String = "") : Exception(message)
class NoPointsException(message: String = "") : Exception(message)

class MultipleStartPointsException(message: String = "Multiple start points found") : Exception(message)
class NoStartPointsException(message: String = "No start points found") : Exception(message)

class MultipleEndPointsException(message: String = "Multiple end points found") : Exception(message)
class NoEndPointsException(message: String = "No end points found") : Exception(message)

class MultipleStartingPathsException(message: String = "Multiple starting paths found"): Exception(message)
class ForkInPathException(message: String = "Fork in path found"): Exception(message)

class UnsetNextDirectionException(message: String = "Next direction was not set correctly"): Exception(message)

class FakeTurnException(message: String = "Fake turn encountered"): Exception(message)

class BrokenPathException(message: String = "Path is broken, unable to follow further"): Exception(message)
