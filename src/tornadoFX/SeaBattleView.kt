import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class SeaBattleView : View(), BoardListener {

    private val columnsNumber = 10

    private val rowsNumber = 10

    private val buttons1 = mutableMapOf<Cell, Button>()

    private val buttons2 = mutableMapOf<Cell, Button>()

    private val buttons3 = mutableMapOf<Cell, Button>()

    private val buttons4 = mutableMapOf<Cell, Button>()

    private lateinit var statusLabel: Label

    private var inProcess = true

    override val root = BorderPane()

    private val board = Board(columnsNumber, rowsNumber)

    var grid1 = GridPane()

    var grid2 = GridPane()

    var button2 = Button()

    init {
        title = "SeaBattle"

        val listener = BoardBasedCellListener(board)

        board.registerListener(this)

        val dimension = Dimension(45.0, Dimension.LinearUnits.px)

        with(root) {
            top {
                vbox {
                    menubar {
                        menu("Game") {
                            item("Exit").action {
                                this@SeaBattleView.close()
                            }
                        }
                    }
                }
            }
            bottom {
                minHeight = 40.0
                statusLabel = label {
                    text = ""
                    font = Font(30.0)
                }
            }
            center {
                button2 = button {
                    style {
                        backgroundColor += Color.ORANGE
                        minWidth = 5 * dimension
                        minHeight = 3 * dimension
                        text = "Старт"
                        textFill = Color.BLUE
                        font = Font(30.0)
                    }
                }
                button2.action {
                    grid1.isVisible = true
                    statusLabel.text = "Ход первого игрока: задайте координаты начала и конца корабля"
                    center {
                        button2 = button {
                            style {
                                backgroundColor += Color.ORANGE
                                minWidth = 5 * dimension
                                minHeight = 3 * dimension
                                text = "Перейти к \n другому игроку"
                                textFill = Color.BLUE
                                font = Font(17.0)
                            }
                        }
                        button2.action {
                            board.firstship = 4
                            board.secondship = 3
                            board.thirdship = 2
                            board.fourthship = 1
                            board.stage = 2
                            board.value = 0
                            button2.isVisible = false
                            grid1.isVisible = false
                            grid2.isVisible = true
                            statusLabel.text = "Ход второго игрока: задайте координаты начала и конца корабля"
                            center {
                                button2 = button {
                                    style {
                                        backgroundColor += Color.ORANGE
                                        minWidth = 5 * dimension
                                        minHeight = 3 * dimension
                                        text = "В БОЙ!"
                                        textFill = Color.BLUE
                                        font = Font(30.0)
                                    }
                                }
                                button2.action {
                                    board.value = 0
                                    statusLabel.text = "Черные - попал, красные - промах"
                                    left {
                                        grid1 = gridpane {
                                            hgap = 6.0
                                            vgap = 6.0
                                            for (row in 0 until rowsNumber) {
                                                row {
                                                    for (column in 0 until columnsNumber) {
                                                        val cell = Cell(column, rowsNumber - 1 - row)
                                                        var button1 = button {
                                                            style {
                                                                backgroundColor += Color.BLUE
                                                                minWidth = dimension
                                                                minHeight = dimension
                                                            }
                                                        }
                                                        button1.action {
                                                            if (inProcess) {
                                                                if (board.value % 2 != 0) {
                                                                    button1 = button1.apply {
                                                                        graphic = circle(radius = 13.0) {
                                                                            fill = when (board.controlship(cell.x, cell.y)) {
                                                                                Chip.NO -> Color.RED
                                                                                Chip.KILL -> Color.BLACK
                                                                                else -> Color.BLUE
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                for (cellkill in board.killship(cell.x, cell.y)) {
                                                                    println(cellkill)
                                                                    buttons3[cellkill] = button {
                                                                        graphic = circle(radius = 13.0) {
                                                                            fill = Color.RED
                                                                        }
                                                                    }
                                                                }
                                                                if (board.win(cell.x, cell.y) == 2) {
                                                                    grid1.isVisible = false
                                                                    grid2.isVisible = false
                                                                    bottom {
                                                                        statusLabel.text = "Второй игрок победил"
                                                                    }
                                                                    center {
                                                                        button1 = button {
                                                                            style {
                                                                                backgroundColor += Color.ORANGE
                                                                                minWidth = 5 * dimension
                                                                                minHeight = 3 * dimension
                                                                                text = "Выход"
                                                                                textFill = Color.BLUE
                                                                                font = Font(30.0)
                                                                            }
                                                                        }
                                                                        button1.action {
                                                                            this@SeaBattleView.close()
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    right {
                                        grid2 = gridpane {
                                            hgap = 6.0
                                            vgap = 6.0
                                            for (row in 0 until rowsNumber) {
                                                row {
                                                    for (column in 0 until columnsNumber) {
                                                        val cell = Cell(column, rowsNumber - 1 - row)
                                                        var button1 = button {
                                                            style {
                                                                backgroundColor += Color.BLUE
                                                                minWidth = dimension
                                                                minHeight = dimension
                                                            }
                                                        }
                                                        button1.action {
                                                            if (inProcess) {
                                                                if (board.value % 2 == 0) {
                                                                    button1 = button1.apply {
                                                                        graphic = circle(radius = 13.0) {
                                                                            fill = when (board.controlship(cell.x, cell.y)) {
                                                                                Chip.NO -> Color.RED
                                                                                Chip.KILL -> Color.BLACK
                                                                                else -> Color.BLUE
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                for (cellkill in board.killship(cell.x, cell.y)) {
                                                                    println(cellkill)
                                                                    buttons4[cellkill] = button {
                                                                        graphic = circle(radius = 13.0) {
                                                                            fill = Color.RED
                                                                        }
                                                                    }
                                                                }
                                                                if (board.win(cell.x, cell.y) == 1) {
                                                                    grid1.isVisible = false
                                                                    grid2.isVisible = false
                                                                    bottom {
                                                                        statusLabel.text = "Первый игрок победил"
                                                                    }
                                                                    center {
                                                                        button1 = button {
                                                                            style {
                                                                                backgroundColor += Color.ORANGE
                                                                                minWidth = 5 * dimension
                                                                                minHeight = 3 * dimension
                                                                                text = "Выход"
                                                                                textFill = Color.BLUE
                                                                                font = Font(30.0)
                                                                            }
                                                                        }
                                                                        button1.action {
                                                                            this@SeaBattleView.close()
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    button2.isVisible = false
                                }
                            }
                            button2.isVisible = false
                        }
                    }
                    button2.isVisible = false
                }
            }
            left {
                grid1 = gridpane {
                    hgap = 6.0
                    vgap = 6.0
                    for (row in 0 until rowsNumber) {
                        row {
                            for (column in 0 until columnsNumber) {
                                val cell = Cell(column, rowsNumber - 1 - row)
                                val button = button {
                                    style {
                                        backgroundColor += Color.BLUE
                                        minWidth = dimension
                                        minHeight = dimension
                                    }
                                }
                                button.action {
                                    if (inProcess) {
                                        listener.cellClicked(cell)
                                        if (board.finishStage()) {
                                            button2.isVisible = true
                                        }
                                    }
                                }
                                buttons1[cell] = button
                            }
                        }
                    }
                }
                grid1.isVisible = false
            }
            right {
                grid2 = gridpane {
                    hgap = 6.0
                    vgap = 6.0
                    for (row in 0 until rowsNumber) {
                        row {
                            for (column in 0 until columnsNumber) {
                                val cell = Cell(column, rowsNumber - 1 - row)
                                val button = button {
                                    style {
                                        backgroundColor += Color.BLUE
                                        minWidth = dimension
                                        minHeight = dimension
                                    }
                                }
                                button.action {
                                    if (inProcess) {
                                        listener.cellClicked(cell)
                                        if (board.finishStage()) {
                                            button2.isVisible = true
                                        }
                                    }
                                }
                                buttons2[cell] = button
                            }
                        }
                    }
                }
                grid2.isVisible = false
            }
        }
        updateBoardAndStatus1()
    }

    override fun turnMade(cell: Cell) {
        updateBoardAndStatus1(cell)
    }

    private fun updateBoardAndStatus1(cell: Cell? = null) {
        if (cell == null) return
        val chip = board[cell]
        if (board.stage == 1) {
            buttons1[cell]?.apply {
                graphic = circle(radius = 13.0) {
                    fill = when (chip) {
                        null -> Color.BLUE
                        Chip.SHIP -> Color.RED
                        else -> Color.BLACK
                    }
                }
            }
        } else buttons2[cell]?.apply {
            graphic = circle(radius = 13.0) {
                fill = when (chip) {
                    null -> Color.BLUE
                    Chip.SHIP -> Color.RED
                    else -> Color.BLACK
                }
            }
        }
    }
}