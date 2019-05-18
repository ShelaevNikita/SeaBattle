import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        Board board = new Board(10, 10);
        System.out.println("Welcome to SeaBattle game!");
        InputStreamReader reader = new InputStreamReader(System.in);
        do {
            System.out.println(board);
            int symTurn;
            do {
                symTurn = reader.read();
            } while (Character.isWhitespace(symTurn));
        } while (board.hasFreeCells());
    }
}