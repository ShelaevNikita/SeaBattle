class BoardBasedCellListener(private val board: Board) : CellListener {

    override fun cellClicked(cell: Cell) {
        board.makeTurn(cell.x, cell.y)
    }
}