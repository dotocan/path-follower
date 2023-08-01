import java.io.File
import java.io.InputStream

fun getMapFromTextFile(filename: String): MutableMap<Int, CharArray> {
    val inputStream: InputStream =
        File("${System.getProperty("user.dir")}/src/main/resources/$filename").inputStream()

    val map: MutableMap<Int, CharArray> = mutableMapOf()
    var index = 0

    inputStream.bufferedReader().forEachLine {
        map[index] = it.toCharArray()
        index++
    }

    return map
}
