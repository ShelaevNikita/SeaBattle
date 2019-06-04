import org.junit.Assert.*
import org.junit.Test

class BoardTest {
    @Test
    fun control() {
        val board = Board(10, 10)
        assertEquals(board.secondship, 3)
        val list = board.makeShip(2, 1, 1, 1)
        assertEquals(board.secondship, 2)
        assertEquals(board.makeShip(4, 1, 3,1), emptyList<Cell>())
        assertEquals(list, listOf(Cell(2, 1), Cell(1, 1)))
        assert(!board.finishStage())
        board.stage = Stage.Second
        assertEquals(board.controlship(1, 1), Chip.KILL)
        assertEquals(board.stage, Stage.Second)
        assertEquals(board.controlship(2, 1), Chip.KILL)
        assertEquals(board.stage, Stage.Second)
        board.killship(1, 1)
        val listkill = board.killship(2, 1)
        assertEquals(listkill, listOf(Cell(0, 0), Cell(0, 1), Cell(0,2), Cell(3, 0),
                Cell(3,1), Cell (3, 2), Cell(1, 2), Cell(1,0),
                Cell(2, 2), Cell(2, 0)))
    }
}