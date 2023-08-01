package models

data class CharactersConfig(
    var startPointChar: Char = '@',
    var endPointChar: Char = 'x',
    var horizontalPathChar: Char = '-',
    var verticalPathChar: Char = '|',
    var intersectionChar: Char = '+'
)