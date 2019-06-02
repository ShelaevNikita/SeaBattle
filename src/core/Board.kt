import java.util.*

import java.lang.Math.*

class Board @JvmOverloads constructor(private val width: Int = 10, private val height: Int = 10) {

    private val chips = HashMap<Cell, Chip>()

    var turn = Chip.SHIP
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

    fun ship(sum: Int): Ships {
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

    fun valueship(ships: Ships): Boolean {
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
        for (cell in list) {
            val neighbor = neighborhood(cell.x, cell.y)
            if (!neighbor) return emptyList()
        }
        if (!valueship(ship)) return emptyList()
        for (cell in list) {
            if (stage == 1) table1[cell.x][cell.y] = ship
            else table2[cell.x][cell.y] = ship
            listfinish += makeTurn(cell.x, cell.y, true)
        }
        return listfinish
    }

    private var firstx = -1

    private var firsty = -1

    var value = 0

    var stage = 1

    private fun makeTurn(x: Int, y: Int, withEvent: Boolean): Cell? {
        val cell = Cell(x, y)
        chips[cell] = turn
        if (listener != null && withEvent) {
            listener!!.turnMade(cell)
        }
        return cell
    }

    fun finishStage() = ((firstship <= 0) && (secondship <= 0) && (thirdship <= 0) && (fourthship <= 0))

    private val controltable1 = MutableList(10) {
        MutableList(10) { false }
    }

    private val controltable2 = MutableList(10) {
        MutableList(10) { false }
    }

    fun controlship(x: Int, y: Int): Chip {
        return if (value % 2 != 0) controlship1(x, y)
        else controlship2(x, y)
    }

    private fun controlship2(x: Int, y: Int): Chip {
        controltable2[x][y] = true
        return if (table2[x][y] == Ships.NO) {
            value++
            Chip.NO
        } else {
            value += 2
            Chip.KILL
        }
    }

    private fun controlship1(x: Int, y: Int): Chip {
        controltable1[x][y] = true
        return if (table1[x][y] == Ships.NO) {
            value++
            Chip.NO
        } else {
            value += 2
            Chip.KILL
        }
    }

    fun killship(x: Int, y: Int): List<Cell> {
        val valuekill = value % 2
        val list = mutableListOf<Cell>()
        val listkill = proverka(x, y)
        if (listkill.isNotEmpty()) {
            val shipfirst = proverka(x, y).first()
            val shipfinish = proverka(x, y).last()
            val firstx = shipfirst.x
            val firsty = shipfirst.y
            val finishx = shipfinish.x
            val finishy = shipfinish.y
            if (valuekill != 0) {
                when (table1[x][y]) {
                    Ships.FIRST -> {
                        if (shipfinish == shipfirst) {
                            if (x + 1 in 0..9) list += Cell(x + 1, y)
                            if (x - 1 in 0..9) list += Cell(x - 1, y)
                            if (y + 1 in 0..9) list += Cell(x, y + 1)
                            if (y - 1 in 0..9) list += Cell(x, y - 1)
                        }
                    }
                    else -> {
                        if (firstx == finishx) {
                            if (firsty - 1 in 0..9) list += Cell(firstx, firsty - 1)
                            if (finishy + 1 in 0..9) list += Cell(shipfinish.x, finishy + 1)
                        } else {
                            if (firstx - 1 in 0..9) list += Cell(firstx - 1, firsty)
                            if (finishx + 1 in 0..9) list += Cell(shipfinish.x + 1, finishy)
                        }
                    }
                }
            } else when (table2[x][y]) {
                Ships.FIRST -> {
                    if (shipfinish == shipfirst) {
                        if (x + 1 in 0..9) list += Cell(x + 1, y)
                        if (x - 1 in 0..9) list += Cell(x - 1, y)
                        if (y + 1 in 0..9) list += Cell(x, y + 1)
                        if (y - 1 in 0..9) list += Cell(x, y - 1)
                    }
                }
                else -> if (firstx == finishx) {
                    if (firsty - 1 in 0..9) list += Cell(firstx, firsty - 1)
                    if (finishy + 1 in 0..9) list += Cell(shipfinish.x, finishy + 1)
                } else {
                    if (firstx - 1 in 0..9) list += Cell(firstx - 1, firsty)
                    if (finishx + 1 in 0..9) list += Cell(shipfinish.x + 1, finishy)
                }
            }
            return list
        }
        return emptyList()
    }

    private fun count(x: Int, y: Int): Int {
        val valuekill = value % 2
        return if (valuekill != 0) {
            when (table1[x][y]) {
                Ships.FIRST -> 0
                Ships.SECOND -> 1
                Ships.THIRD -> 2
                Ships.FOURTH -> 3
                else -> -1
            }
        } else when (table2[x][y]) {
            Ships.FIRST -> 0
            Ships.SECOND -> 1
            Ships.THIRD -> 2
            Ships.FOURTH -> 3
            else -> -1
        }
    }

    private fun proverka(x: Int, y: Int): List<Cell> {
        val valuekill = value % 2
        val ships = if (valuekill != 0) table1[x][y] else table2[x][y]
        val count = count(x, y)
        var kill = 0
        val list = mutableListOf<Cell>()
        if (count == -1) return emptyList()
        for (a in -count..count) {
            if (x + a in 0..9) when (valuekill) {
                1 -> if ((table1[x + a][y] == ships) && controltable1[x + a][y]) {
                    kill++
                    list += Cell(x + a, y)
                    if (kill == count + 1) {
                        return list
                    }
                }
                0 -> if ((table2[x + a][y] == ships) && controltable2[x + a][y]) {
                    kill++
                    list += Cell(x + a, y)
                    if (kill == count + 1) {
                        return list
                    }
                }
            }
        }
        kill = 0
        list.clear()
        for (b in -count..count) {
            if (y + b in 0..9) when (valuekill) {
                1 -> if ((table1[x][y + b] == ships) && controltable1[x][y + b]) {
                    kill++
                    list += Cell(x, y + b)
                    if (kill == count + 1) {
                        return list
                    }
                }
                0 -> if ((table2[x][y + b] == ships) && controltable2[x][y + b]) {
                    kill++
                    list += Cell(x, y + b)
                    if (kill == count + 1) {
                        return list
                    }
                }
            }
        }
        return emptyList()
    }

    private var shotkill1 = 0

    private var shotkill2 = 0

    fun win(x: Int, y: Int): Int {
        val shot = proverka(x, y).size
        if (value % 2 == 0) {
            shotkill1 += shot
        } else {
            shotkill2 += shot
        }
        if (shotkill1 >= 20) return 1
        if (shotkill2 >= 20) return 2
        return 0
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (y in height - 1..0) {
            for (x in 0 until width) {
                val chip = get(x, y)
                if (chip == null) {
                    sb.append("- ")
                    continue
                }
                if (chip == Chip.SHIP) sb.append("Ship")
            }
            sb.append("\n")
        }
        return sb.toString()
    }
}
