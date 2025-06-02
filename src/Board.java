import java.util.Random;

public class Board {
    private Cell[][] cells;
    private int size;
    private int mines;

    public Board(int size, int mines) {
        this.size = size;
        this.mines = mines;
        this.cells = new Cell[size][size];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                cells[r][c] = new Cell();
            }
        }

        placeMines();
        countAdjacentMines();
    }

    public int getSize() {
        return size;
    }

    public Cell getCell(int r, int c) {
        return cells[r][c];
    }

    private void placeMines() {
        Random rand = new Random();
        int placed = 0;
        while (placed < mines) {
            int r = rand.nextInt(size);
            int c = rand.nextInt(size);
            if (!cells[r][c].hasMine) {
                cells[r][c].hasMine = true;
                placed++;
            }
        }
    }

    private void countAdjacentMines() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (cells[r][c].hasMine) continue;

                int count = 0;
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr;
                        int nc = c + dc;

                        if (nr >= 0 && nr < size && nc >= 0 && nc < size) {
                            if (cells[nr][nc].hasMine) {
                                count++;
                            }
                        }
                    }
                }
                cells[r][c].adjacentMines = count;
            }
        }
    }

    public void reveal(int r, int c) {
        if (r < 0 || r >= size || c < 0 || c >= size || cells[r][c].revealed || cells[r][c].isMarked) {
            return;
        }

        cells[r][c].revealed = true;

        if (cells[r][c].adjacentMines == 0) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    reveal(r + dr, c + dc);
                }
            }
        }
    }

    public void revealAll() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                cells[r][c].revealed = true;
            }
        }
    }

    public boolean checkWin() {
        int safeCells = size * size - mines;
        int revealedSafeCells = 0;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (cells[r][c].revealed && !cells[r][c].hasMine) {
                    revealedSafeCells++;
                }
            }
        }
        return revealedSafeCells == safeCells;
    }

    public void printMines() {
        System.out.println("--- Mine Locations ---");
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (cells[r][c].hasMine) {
                    System.out.println("Mine at (" + r + ", " + c + ")");
                }
            }
        }
        System.out.println("----------------------");
    }

    public void printBoard() {
        System.out.print("\n   ");
        for (int c = 0; c < size; c++) {
            System.out.print(" " + c + " ");
        }
        System.out.println();

        for (int r = 0; r < size; r++) {
            System.out.print(r + "  ");
            for (int c = 0; c < size; c++) {
                Cell cell = cells[r][c];
                if (cell.revealed) {
                    if (cell.hasMine) {
                        System.out.print(" 💥");
                    } else if (cell.adjacentMines == 0) {
                        System.out.print("   ");
                    } else {
                        System.out.print(" " + cell.adjacentMines + " ");
                    }
                } else if (cell.isMarked) {
                    System.out.print(" 🚩");
                } else {
                    System.out.print(" - ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}