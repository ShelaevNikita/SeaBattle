class BoardBasedCellListener(private val board: Board) : CellListener {

    override fun cellClicked(cell: Cell) {
        board.makeTurn(cell.x, cell.y)
    }

    override fun killship(cell: Cell) {
        board.killship(cell.x, cell.y)
    }
}
