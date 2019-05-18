public final class Cell {

    private final int x;

    private final int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other instanceof Cell) {
            Cell cell = (Cell) other;
            return x == cell.x && y == cell.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 13;
        result = 17 * result + x;
        return 17 * result + y;
    }

    @Override
    public String toString() {
        return "" + x + ":" + y;
    }
}