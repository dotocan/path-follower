import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class MapProviderTest {
    @Test
    fun `readFileToMap should read input data from a text file and create a map out of it`() {
        val expectedRow1 = charArrayOf('@', '-', '-', '-', 'A', '-', '-', '-', '+')
        val expectedRow2 = charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '|')
        val expectedRow3 = charArrayOf('x', '-', 'B', '-', '+', ' ', ' ', ' ', 'C')
        val expectedRow4 = charArrayOf(' ', ' ', ' ', ' ', '|', ' ', ' ', ' ', '|')
        val expectedRow5 = charArrayOf(' ', ' ', ' ', ' ', '+', '-', '-', '-', '+')

        val expectedMap: MutableMap<Int, CharArray> = mutableMapOf()
        expectedMap[0] = expectedRow1
        expectedMap[1] = expectedRow2
        expectedMap[2] = expectedRow3
        expectedMap[3] = expectedRow4
        expectedMap[4] = expectedRow5

        val actualMap = getMapFromTextFile("basic.txt")

        expectedMap.forEach { (index, row) ->
            assertContentEquals(row, actualMap[index])
        }
    }
}