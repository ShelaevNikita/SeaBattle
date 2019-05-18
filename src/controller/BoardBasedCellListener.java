public class BoardBasedCellListener implements CellListener {

    private final Board board;

    public BoardBasedCellListener(Board board) {
        this.board = board;
    }

    public void cellClicked(Cell cell) {
        board.makeTurn(cell.getX(), cell.getY());
    }
}