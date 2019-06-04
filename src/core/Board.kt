import java.util.*

import java.lang.Math.*

class Board @JvmOverloads constructor(private val width: Int = 10, private val height: Int = 10) {

    private val chips = HashMap<Cell, Chip>()

    private var turn = Chip.SHIP

    private var turnkill = Chip.KILL

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

    private val table1 = MutableList(height) {
        MutableList(width) { Ships.NO }
    }

    private val table2 = MutableList(height) {
        MutableList(width) { Ships.NO }
    }

    private var table = table1

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
                if ((x + a in 0 until width) && (y + b in 0 until height)) {
                    if (table[x + a][y + b] != Ships.NO) neighbor++
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

    private fun valueship(ships: Ships): Boolean {
        when (ships) {
            Ships.FIRST -> {
                if (firstship == 0) return false
                firstship--
            }
            Ships.SECOND -> {
                if (secondship == 0) return false
                secondship--
            }
            Ships.THIRD -> {
                if (thirdship == 0) return false
                thirdship--
            }
            Ships.FOURTH -> {
                if (fourthship == 0) return false
                fourthship--
            }
            else -> return false
        }
        return true
    }

    fun makeShip(x1: Int, y1: Int, x2: Int, y2: Int): List<Cell?> {
        val list = mutableListOf<Cell>()
        val listfinish = mutableListOf<Cell?>()
        val sum = sum(x1, y1, x2, y2)
        val ship = ship(sum)
        if (x1 == x2) {
            if (y1 <= y2) for (s in 0..sum)
                list += Cell(x1, y1 + s)
            else for (s in 0..sum)
                list += Cell(x1, y1 - s)
        } else {
            if (x1 <= x2) for (s in 0..sum)
                list += Cell(x1 + s, y1)
            else for (s in 0..sum)
                list += Cell(x1 - s, y1)
        }
        for (cell in list) {
            val neighbor = neighborhood(cell.x, cell.y)
            if (!neighbor) return emptyList()
        }
        if (!valueship(ship)) return emptyList()
        for (cell in list) {
            table[cell.x][cell.y] = ship
            listfinish += makeship(cell.x, cell.y, true)
        }
        return listfinish
    }

    var stage = Stage.TurnFirst

    var value = 1

    private fun makeship(x: Int, y: Int, withEvent: Boolean): Cell? {
        val cell = Cell(x, y)
        chips[cell] = turn
        if (withEvent) {
            listener?.turnMade(cell)
        }
        return cell
    }

    fun finishStage(): Boolean {
        return if ((firstship == 0) && (secondship == 0) && (thirdship == 0) && (fourthship == 0)) {
            value--
            if (value == 0) {
                table = table2
                firstship = 4
                secondship = 3
                thirdship = 2
                fourthship = 1
            }
            stage = if (stage == Stage.TurnFirst) Stage.TurnSecond else Stage.First
            true
        } else false
    }

    private val controltable1 = MutableList(height) {
        MutableList(width) { false }
    }

    private val controltable2 = MutableList(height) {
        MutableList(width) { false }
    }

    private var controltable = controltable2

    fun controlship(x: Int, y: Int): Chip {
        return controlship1(x, y)
    }

    private fun controlship1(x: Int, y: Int): Chip {
        controltable[x][y] = true
        return if (table[x][y] == Ships.NO) {
            if (stage == Stage.First) {
                stage = Stage.Second
                controltable = controltable1
                table = table1
            } else {
                stage = Stage.First
                controltable = controltable2
                table = table2
            }
            Chip.NO
        } else {
            Chip.KILL
        }
    }

    fun killship(x: Int, y: Int): List<Cell?> {
        val list = mutableListOf<Cell>()
        val listfinish = mutableListOf<Cell?>()
        val listkill = proverka(x, y)
        if (listkill.isNotEmpty()) {
            val first = listkill.first()
            val last = listkill.last()
            if (first.x == last.x) {
                if (first.y - 1 in 0 until width) {
                    for (a in first.x - 1..first.x + 1)
                        if (a in 0 until height) list += Cell(a, first.y - 1)
                }
                if (last.y + 1 in 0 until width) {
                    for (a in last.x - 1..last.x + 1)
                        if (a in 0 until height) list += Cell(a, last.y + 1)
                }
                for (kill in listkill) {
                    if (kill.x + 1 in 0 until width) list += Cell(kill.x + 1, kill.y)
                    if (kill.x - 1 in 0 until width) list += Cell(kill.x - 1, kill.y)
                }
            } else {
                if (first.x - 1 in 0 until height) {
                    for (b in first.y - 1..first.y + 1)
                        if (b in 0 until width) list += Cell(first.x - 1, b)
                }
                if (last.x + 1 in 0 until height) {
                    for (b in last.y - 1..last.y + 1)
                        if (b in 0 until width) list += Cell(last.x + 1, b)
                }
                for (kill in listkill) {
                    if (kill.y + 1 in 0 until height) list += Cell(kill.x, kill.y + 1)
                    if (kill.y - 1 in 0 until height) list += Cell(kill.x, kill.y - 1)
                }
            }
            for (cell in list)
                listfinish += makeshipkill(cell.x, cell.y, true)
        }
        return listfinish
    }

    private fun makeshipkill(x: Int, y: Int, withEvent: Boolean): Cell? {
        val cell = Cell(x, y)
        chips[cell] = turnkill
        if (withEvent) {
            listener?.killship(cell)
        }
        return cell
    }

    private fun count(x: Int, y: Int): Int {
        return when (table[x][y]) {
            Ships.FIRST -> 1
            Ships.SECOND -> 2
            Ships.THIRD -> 3
            Ships.FOURTH -> 4
            else -> -1
        }
    }

    private fun proverka(x: Int, y: Int): List<Cell> {
        val ships = table[x][y]
        val count = count(x, y)
        var kill = 0
        val list = mutableListOf<Cell>()
        if (count == -1) return emptyList()
        for (a in -count + 1 until count) {
            if (x + a in 0 until width) {
                if ((table[x + a][y] == ships) && controltable[x + a][y]) {
                    kill++
                    list += Cell(x + a, y)
                    if (kill == count) {
                        return list
                    }
                }
            }
        }
        kill = 0
        list.clear()
        for (b in -count + 1 until count) {
            if (y + b in 0 until height) {
                if ((table[x][y + b] == ships) && controltable[x][y + b]) {
                    kill++
                    list += Cell(x, y + b)
                    if (kill == count) {
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
        if (stage == Stage.First) {
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