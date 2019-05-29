import java.util.*

import java.lang.Math.*

class Board @JvmOverloads constructor(private val width: Int = 10, private val height: Int = 10) {

    private val chips = HashMap<Cell, Chip>()

    var turn = Chip.KRESTIC
        private set

    var listener: BoardListener? = null
        private set

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

    private val table1 = MutableList(10) {
        MutableList(10) { Ships.NO }
    }

    private val table2 = MutableList(10) {
        MutableList(10) { Ships.NO }
    }

    private fun sum(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return when {
            (x2 == x1) -> abs(y2 - y1)
            (y2 == y1) -> abs(x2 - x1)
            else -> -1
        }
    }

    private fun ship(sum: Int): Ships {
        return when (sum) {
            0 -> Ships.FIRST
            1 -> Ships.SECOND
            2 -> Ships.THIRD
            3 -> Ships.FOURTH
            else -> Ships.NO
        }
    }

    private fun neighborhood(x: Int, y: Int): Boolean {
        var neighbor = 0
        for (a in -1..1) {
            for (b in -1..1) {
                if ((x + a in 0..9) && (y + b in 0..9)) when (stage) {
                    1 -> if (table1[x + a][y + b] != Ships.NO) neighbor++
                    2 -> if (table2[x + a][y + b] != Ships.NO) neighbor++
                }
                if (neighbor != 0) return false
            }
        }
        return true
    }

    var firstship = 4

    var secondship = 3

    var thirdship = 2

    var fourthship = 1

    private fun valueship (ships: Ships): Boolean{
        when (ships) {
            Ships.FIRST -> {
                firstship--
                if (firstship < 0) return false
            }
            Ships.SECOND -> {
                secondship--
                if (secondship < 0) return false
            }
            Ships.THIRD -> {
                thirdship--
                if (thirdship < 0) return false
            }
            Ships.FOURTH -> {
                fourthship--
                if (fourthship < 0) return false
            }
            else -> return false
        }
        return true
    }

    fun makeTurn(x: Int, y: Int): List<Cell?> {
        val list = mutableListOf<Cell>()
        val listfinish = mutableListOf<Cell?>()
        var sum = -1
        if (value % 2 == 0) {
            firstx = x
            firsty = y
        } else sum = sum(firstx, firsty, x, y)
        value++
        val ship = ship(sum)
        if (firstx == x) {
            if (firsty <= y) for (s in 0..sum)
                list += Cell(firstx, firsty + s)
            else for (s in 0..sum)
                list += Cell(firstx, firsty - s)
        } else {
            if (firstx <= x) for (s in 0..sum)
                list += Cell(firstx + s, firsty)
            else for (s in 0..sum)
                list += Cell(firstx - s, firsty)
        }
        for (cell in list){
            val smoke = neighborhood(cell.x, cell.y)
            if (!smoke) return emptyList()
        }
        if (!valueship(ship)) return emptyList()
        for (cell in list) {
            if (stage == 1) table1[cell.x] [cell.y] = ship
            else table2[cell.x] [cell.y] = ship
            number += sum
            listfinish += makeTurn(cell.x, cell.y, true)
        }
        return listfinish
    }

    private var firstx = -1

    private var firsty = -1

    private var number = 0

    var value = 0

    var stage = 1

    private fun makeTurn(x: Int, y: Int, withEvent: Boolean): Cell? {
        val cell = Cell(x, y)
        if (!chips.containsKey(cell)) {
            chips[cell] = turn
            if (listener != null && withEvent) {
                listener!!.turnMade(cell)
            }
            return cell
        }
        return null
    }

    fun finishStage () = number == 20
}
