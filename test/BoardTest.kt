import org.junit.Assert.*
import org.junit.Test

class BoardTest {
    @Test
    fun control() {
        val board = Board(10, 10)
        val ship = board.ship(2)
        assert(board.valueship(ship))
        assertEquals(board.thirdship, 1)
        assertEquals(board.secondship, 3)
        board.makeTurn(1, 1)
        val list = board.makeTurn(2, 1)
        assertEquals(board.secondship, 2)
        board.makeTurn(3, 1)
        assertEquals(board.makeTurn(4, 1), emptyList<Cell>())
        assertEquals(list, listOf(Cell(1, 1), Cell(2, 1)))
        assert(!board.finishStage())
        board.value++
        var count = board.value
        assertEquals(board.controlship(1, 1), Chip.KILL)
        assertEquals(board.value, count + 2)
        count = board.value
        assertEquals(board.controlship(2, 1), Chip.KILL)
        assertEquals(board.value, count + 2)
        count = board.value
        assertEquals(board.controlship(3, 1), Chip.NO)
        assertEquals(board.value, count + 1)
        board.value++
        board.killship(1, 1)
        val listkill = board.killship(2, 1)
        assertEquals(listkill, listOf(Cell(0, 1), Cell(3, 1)))
    }
}