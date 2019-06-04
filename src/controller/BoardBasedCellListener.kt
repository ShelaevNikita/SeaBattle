class BoardBasedCellListener(private val board: Board) : CellListener {

    override fun cellClicked(cell1: Cell, cell2: Cell) {
        board.makeShip(cell1.x, cell1.y, cell2.x, cell2.y)
    }

    override fun killship(cell: Cell) {
        board.killship(cell.x, cell.y)
    }
}
