import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import tornadofx.*

class SeaBattleView : View(), BoardListener {

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

        with (root) {
            top {
                vbox {
                    menubar {
                        menu("Game") {
                            item("Restart").action {
                                restartGame()
                            }
                            separator()
                            item("Exit").action {
                                this@SeaBattleView.close()
                            }
                        }
                    }
                }
            }
            center {
                gridpane {
                    hgap = 5.0
                    vgap = 5.0
                    val dimension = Dimension(50.0, Dimension.LinearUnits.px)
                    for (row in 0 until rowsNumber) {
                        row {
                            for (column in 0 until columnsNumber) {
                                val cell = Cell(column, rowsNumber - 1 - row)
                                val button = button {
                                    style {
                                        backgroundColor += Color.GRAY
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
        for (x in 0 until columnsNumber) {
            for (y in 0 until rowsNumber) {
                updateBoardAndStatus(Cell(x, y))
            }
        }
        inProcess = true
    }
    override fun turnMade(cell: Cell) {
        updateBoardAndStatus(cell)
    }

    private fun updateBoardAndStatus(cell: Cell? = null) {
        if (cell == null) return
        val chip = board[cell]
        buttons[cell]?.apply {
            graphic = circle(radius = 24.0) {
                fill = when (chip) {
                    null -> Color.GRAY
                    else -> Color.RED
                }
            }
        }
    }
}
