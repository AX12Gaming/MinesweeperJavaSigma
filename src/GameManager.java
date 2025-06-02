import java.util.*;

public class GameManager {
    private int SIZE;
    private int MINES;
    private ArrayList<Player> players = new ArrayList<>();

    public void setupPlayers(Scanner input) {
        System.out.println("How many players? (Max 4):");
        int numPlayers;
        while (true) {
            if (input.hasNextInt()) {
                numPlayers = input.nextInt();
                input.nextLine();
                if (numPlayers > 0 && numPlayers < 5) {
                    break;
                }
            } else {
                input.nextLine();
            }
            System.out.println("Invalid amount of players, Try again!");
        }

        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Enter name for player " + (i + 1) + ":");
            String name = input.nextLine();
            players.add(new Player(name));
        }
    }

    public void setupGame(Scanner input) {
        while (true) {
            System.out.println("Enter board size (1-10):");
            if (input.hasNextInt()) {
                SIZE = input.nextInt();
                input.nextLine();
                if (SIZE >= 1 && SIZE <= 10) break;
            } else {
                input.nextLine();
            }
            System.out.println("Invalid input. Try again.");
        }

        MINES = SIZE;

        System.out.println("Choose difficulty: Easy, Normal, Or Hard");
        String difficulty;
        while (true) {
            difficulty = input.nextLine().trim().toUpperCase();

            if (difficulty.equals("EASY")) {
                MINES = MINES / 2;
                break;
            } else if (difficulty.equals("HARD")) {
                MINES = MINES + MINES / 2;
                break;
            } else if (difficulty.equals("NORMAL")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter Easy, Normal, Or Hard.");
            }
        }

        for (Player player : players) {
            player.setBoard(new Board(SIZE, MINES));
        }

        printInstructions();
    }

    public void play(Scanner input) {
        int currentPlayerIndex = 0;

        while (true) {
            Player player = players.get(currentPlayerIndex);
            Board currentBoard = player.getBoard();

            if (players.size() > 1) {
                System.out.println("\n--- " + player.getName() + "'s turn ---");
            }
            currentBoard.printBoard();

            System.out.print("Enter M to mark/unmark or S to show: ");
            String action = input.nextLine().trim().toUpperCase();

            if (action.equals("QUIT")) {
                System.out.println("Goodbye!");
                break;
            }

            if (action.equals("GOD")) {
                currentBoard.printMines();
                continue;
            }

            if (!action.equals("M") && !action.equals("S")) {
                System.out.println("Invalid action. Please enter M or S.");
                continue;
            }

            System.out.print("Enter row and col (0-" + (SIZE - 1) + "): ");
            try {
                int r = input.nextInt();
                int c = input.nextInt();
                input.nextLine();

                if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) {
                    System.out.println("Invalid coordinates. Row and Column must be between 0 and " + (SIZE - 1) + ".");
                    continue;
                }

                Cell currentCell = currentBoard.getCell(r, c);

                if (action.equals("M")) {
                    if (currentCell.revealed) {
                        System.out.println("Cannot mark a revealed cell.");
                        continue;
                    }
                    currentCell.isMarked = !currentCell.isMarked;
                    System.out.println(currentCell.isMarked ? "Cell (" + r + ", " + c + ") marked!" : "Cell (" + r + ", " + c + ") unmarked.");
                } else {
                    if (currentCell.isMarked) {
                        System.out.println("Cannot show a marked cell. Unmark it first.");
                        continue;
                    }

                    if (currentCell.hasMine) {
                        System.out.println(player.getName() + " hit a mine at (" + r + ", " + c + ")!");
                        currentBoard.revealAll();
                        currentBoard.printBoard();
                        System.out.println("Game Over for " + player.getName() + "!");
                        break;
                    } else {
                        currentBoard.reveal(r, c);
                        if (currentBoard.checkWin()) {
                            currentBoard.printBoard();
                            System.out.println("\nCongratulations, " + player.getName() + "! You cleared your board!");
                            break;
                        }
                    }
                }

                player.incrementMoves();

                if (players.size() > 1) {
                    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter numbers for row and column.");
                input.nextLine();
            }
        }

        System.out.println("\n--- Final Stats ---");
        for (Player p : players) {
            System.out.println(p.getName() + ": " + p.getMoves() + " moves");
        }

        System.out.println("Thanks for playing!");
    }

    private void printInstructions() {
        System.out.println("\n--- Game Instructions ---");
        System.out.println("- Goal: Reveal safe spots without hitting a mine.");
        System.out.println("- Action 'M': Mark/unmark a cell you suspect has a mine (e.g., M 2 3).");
        System.out.println("- Action 'S': Show/reveal a cell (e.g., S 1 1).");
        System.out.println("- Special Commands:");
        System.out.println("  - Enter 'GOD' to temporarily see all mine locations (cheat code).");
        System.out.println("  - Enter 'QUIT' to exit the game at any time.");
        System.out.println("-------------------------\n");
    }
}