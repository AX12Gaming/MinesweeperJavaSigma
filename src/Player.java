public class Player {
    private String name;
    private int moves;
    private Board playerBoard;

    public Player(String name) {
        this.name = name;
        this.moves = 0;
    }

    public String getName() {
        return name;
    }

    public int getMoves() {
        return moves;
    }

    public void incrementMoves() {
        moves++;
    }

    public void setBoard(Board board) {
        this.playerBoard = board;
    }

    public Board getBoard() {
        return playerBoard;
    }
}