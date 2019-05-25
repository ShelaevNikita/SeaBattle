import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import tornadofx.*

class SeaBattleView3 : View(), BoardListener {

    private val columnsNumber = 10

    private val rowsNumber = 10

    private val buttons = mutableMapOf<Cell, Button>()

    private var inProcess = true

    override val root = BorderPane()

    private val board = Board(columnsNumber, rowsNumber)

    init {
        title = "SeaBattle"
        val listener = BoardBasedCellListener(board)
        board.registerListener(this)

        with(root) {
            top {
                vbox {
                    menubar {
                        menu("Game") {
                            item("Restart").action {
                                restartGame()
                            }
                            separator()
                            item("Exit").action {
                                this@SeaBattleView3.close()
                            }
                        }
                    }
                }
            }
            center {
                gridpane {
                    hgap = 5.0
                    vgap = 5.0
                    val dimension = Dimension(45.0, Dimension.LinearUnits.px)
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
                                    }
                                }
                                buttons[cell] = button
                            }
                        }
                    }
                }
            }
        }
    }

    private fun restartGame() {
        board.clear()
        replaceWith<SeaBattleView1>()
        board.firstship = 4
        board.secondship = 3
        board.thirdship = 2
        board.fourthship = 1
        inProcess = true
    }

    override fun turnMade(cell: Cell) {
        updateBoardAndStatus(cell)
    }

    private fun updateBoardAndStatus(cell: Cell? = null) {
        if (cell == null) return
        val chip = board[cell]
        buttons[cell]?.apply {
            graphic = circle(radius = 20.0) {
                fill = when (chip) {
                    null -> Color.BLUE
                    else -> Color.RED
                }
            }
        }
    }
}