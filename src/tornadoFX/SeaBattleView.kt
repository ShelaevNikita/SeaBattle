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

    private var cellfirst = Cell(0, 0)

    private var stage = Stage.FirstCell

    private var inProcess = true

    override val root = BorderPane()

    private val board = Board(columnsNumber, rowsNumber)

    private var grid1 = GridPane()

    private var grid2 = GridPane()

    private var button2 = Button()

    private var button3 = Button()

    private val set1 = mutableSetOf<Button?>()

    private val set2 = mutableSetOf<Button?>()

    private val set3 = mutableSetOf<Button?>()

    private val set4 = mutableSetOf<Button?>()

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
                                minWidth = 6 * dimension
                                minHeight = 2.5 * dimension
                                text = "   Перейти к \n другому игроку"
                                textFill = Color.BLUE
                                font = Font(20.0)
                            }
                        }
                        button2.action {
                            button2.isVisible = false
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
                                    statusLabel.text = "Черные - попал, красные - промах." +
                                            " Ход первого игрока --->>>"
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
                                                            if ((button1 !in set2) && (inProcess)) {
                                                                if (board.stage == Stage.Second) {
                                                                    val control = board.controlship(cell.x, cell.y)
                                                                    if (button1 !in set2) button1 = button1.apply {
                                                                        graphic = circle(radius = 13.0) {
                                                                            fill = when (control) {
                                                                                Chip.NO -> Color.RED
                                                                                Chip.KILL -> Color.BLACK
                                                                                else -> Color.BLUE
                                                                            }
                                                                        }
                                                                    }
                                                                    set2 += button1
                                                                    if (control == Chip.KILL)
                                                                        statusLabel.text = "Ход второго игрока <<<---"
                                                                    else statusLabel.text = "Ход первого игрока --->>>"
                                                                }
                                                                listener.killship(cell)
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
                                                        buttons3[cell] = button1
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
                                                            if ((button1 !in set1) && (inProcess)) {
                                                                if (board.stage == Stage.First) {
                                                                    val control = board.controlship(cell.x, cell.y)
                                                                    button1 = button1.apply {
                                                                        graphic = circle(radius = 13.0) {
                                                                            fill = when (control) {
                                                                                Chip.NO -> Color.RED
                                                                                Chip.KILL -> Color.BLACK
                                                                                else -> Color.BLUE
                                                                            }
                                                                        }
                                                                    }
                                                                    set1 += button1
                                                                    if (control == Chip.KILL)
                                                                        statusLabel.text = "Ход первого игрока --->>>"
                                                                    else statusLabel.text = "Ход второго игрока <<<---"
                                                                }
                                                                listener.killship(cell)
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
                                                        buttons4[cell] = button1
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
                                    if ((button !in set3) && (inProcess)) {
                                        if (stage == Stage.FirstCell) {
                                            button3 = button
                                            button3.apply {
                                                graphic = circle(radius = 7.0) {
                                                    fill = Color.RED
                                                }
                                            }
                                            cellfirst = cell
                                            stage = Stage.LastCell
                                        } else {
                                            button3.apply {
                                                graphic = circle(radius = 7.0) {
                                                    fill = Color.BLUE
                                                }
                                            }
                                            listener.cellClicked(cellfirst, cell)
                                            stage = Stage.FirstCell
                                            statusLabel.text = "1-ых - ${board.firstship}; 2-ых - ${board.secondship}; " +
                                                    " 3-ых - ${board.thirdship}; 4-ых - ${board.fourthship}"
                                        }
                                        if (board.finishStage()) {
                                            button2.isVisible = true
                                            grid1.isVisible = false
                                            statusLabel.text = "Нажмите на кнопку, чтобы перейти ко второму игроку"
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
                                    if ((button !in set4) && (inProcess)) {
                                        if (stage == Stage.FirstCell) {
                                            cellfirst = cell
                                            button3 = button
                                            button3.apply {
                                                graphic = circle(radius = 7.0) {
                                                    fill = Color.RED
                                                }
                                            }
                                            stage = Stage.LastCell
                                        } else {
                                            button3.apply {
                                                graphic = circle(radius = 7.0) {
                                                    fill = Color.BLUE
                                                }
                                            }
                                            listener.cellClicked(cellfirst, cell)
                                            stage = Stage.FirstCell
                                            statusLabel.text = "1-ых - ${board.firstship}; 2-ых - ${board.secondship}; " +
                                                    " 3-ых - ${board.thirdship}; 4-ых - ${board.fourthship}"
                                        }
                                        if (board.finishStage()) {
                                            button2.isVisible = true
                                            grid2.isVisible = false
                                            statusLabel.text = "Нажмите на кнопку, чтобы начать бой"
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

    override fun killship(cell: Cell) {
        updateBoardAndStatus2(cell)
    }

    private fun updateBoardAndStatus1(cell: Cell? = null) {
        if (cell == null) return
        val chip = board[cell]
        if (board.stage == Stage.TurnFirst) {
            set3 += buttons1[cell]
            buttons1[cell]?.apply {
                graphic = circle(radius = 13.0) {
                    fill = when (chip) {
                        null -> Color.BLUE
                        Chip.SHIP -> Color.RED
                        else -> Color.BLACK
                    }
                }
            }
        } else {
            set4 += buttons2[cell]
            buttons2[cell]?.apply {
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

    private fun updateBoardAndStatus2(cell: Cell? = null) {
        if (cell == null) return
        val chip = board[cell]
        if (board.stage == Stage.First) {
            set1 += buttons4[cell]
            buttons4[cell]?.apply {
                graphic = circle(radius = 13.0) {
                    fill = when (chip) {
                        null -> Color.BLUE
                        Chip.KILL -> Color.RED
                        else -> Color.BLACK
                    }
                }
            }
        } else {
            set2 += buttons3[cell]
            buttons3[cell]?.apply {
                graphic = circle(radius = 13.0) {
                    fill = when (chip) {
                        null -> Color.BLUE
                        Chip.KILL -> Color.RED
                        else -> Color.BLACK
                    }
                }
            }
        }
    }
}
