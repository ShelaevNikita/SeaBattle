import java.util.*

import java.lang.Math.*

class Board @JvmOverloads constructor(private val width: Int = 10, private val height: Int = 10) {

    private val chips = HashMap<Cell, Chip>()

    var turn = Chip.KRESTIC
        private set

    var listener: BoardListener? = null
        private set

    var table: MutableList<List<Int>> = ArrayList()

    var tableList: MutableList<Int> = ArrayList()

    fun registerListener(listener: BoardListener) {
        this.listener = listener
    }

    operator fun get(x: Int, y: Int): Chip? {
        return get(Cell(x, y))
    }

    operator fun get(cell: Cell): Chip? {
        return chips[cell]
    }

    fun clear() {
        chips.clear()
        turn = Chip.KRESTIC
    }

    fun hasFreeCells(): Boolean {
        for (x in 0 until width) {
            for (y in 0 until height) {
                if (get(x, y) == null) return true
            }
        }
        return false
    }

    private fun sum(x1: Int, y1: Int, x2: Int, y2: Int): Int? {
        return when {
            (x2 == x1) -> abs(y2 - y1)
            (y2 == y1) -> abs(x2 - x1)
            else -> null
        }
    }

    fun makeTurn(x: Int, y: Int): Cell? {
        var sum = -1
        if (value % 2 == 0) {
            cell1 = Cell(x, y)
        } else {
            val sum1 = sum(cell1.x, cell1.y, x, y)
            if (sum1 == null) return null else sum = sum1
        }
        value++
        if (sum in 0..3) return makeTurn(x, y, true)
        return null
    }

    private var cell1 = Cell(-1, -1)

    private var value = 0

    private fun makeTurn(x: Int, y: Int, withEvent: Boolean): Cell? {
        val cell = Cell(x, y)
        if (!chips.containsKey(cell)) {
            chips[cell] = turn
            turn = turn.opposite()
            if (listener != null && withEvent) {
                listener!!.turnMade(cell)
            }
            return cell
        }
        return null
    }
}
