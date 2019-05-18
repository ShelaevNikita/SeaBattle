import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class Board {

    private final int width;

    private final int height;

    private final Map<Cell, Chip> chips = new HashMap<Cell, Chip>();

    private Chip turn = Chip.KRESTIC;

    private BoardListener listener = null;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Board() {
        this(10, 10);
    }

    public void registerListener(BoardListener listener) {
        this.listener = listener;
    }

    public Chip get(int x, int y) {
        return get(new Cell(x, y));
    }

    public Chip get(Cell cell) {
        return chips.get(cell);
    }

    public Chip getTurn() {
        return turn;
    }

    public void clear() {
        chips.clear();
        turn = Chip.KRESTIC;
    }

    public BoardListener getListener() {
        return listener;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean hasFreeCells() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (get(x, y) == null) return true;
            }
        }
        return false;
    }

    public List<List<Integer>> table = new ArrayList<List<Integer>>();

    public List<Integer> tableList = new ArrayList<Integer>();

    public int value = 0;

    private List<List<Integer>> Table (){
        for (int i = 0; i < 10; i++){
            for (int j = 0; j < 10; j++) {
                tableList.add(0);
            }
            table.add(tableList);
        }
        return table;
    }

    public int sum (int x, int y){
        table = Table();
        int sum = 0;
        for (int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++) {
                sum += table.get(x + i).get(y + j);
            }
        }
        return sum;
    }

    public Cell makeTurn(int x, int y) {
        value++;
        if (value < 21) return makeTurn(x, y, true);
        else return null;
    }

    private Cell makeTurn(int x, int y, boolean withEvent) {
        Cell cell = new Cell(x, y);
        if (!chips.containsKey(cell)) {
            chips.put(cell, turn);
            turn = turn.opposite();
            if (listener != null && withEvent) {
                listener.turnMade(cell);
            }
            return cell;
        }
        return null;
    }
}
