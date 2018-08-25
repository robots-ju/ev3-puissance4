package p4;

public class Grid {
	public static final int WIDTH = 7;
	public static final int HEIGHT = 6;

	public static final int N_IN_A_ROW_TO_WIN = 4;

	private Piece grid[][] = new Piece[WIDTH][HEIGHT];
	private boolean computerTurn = false;

	public Grid() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				grid[x][y] = Piece.EMPTY;
			}
		}
	}

	public Piece get(int x, int y) {
		return grid[x][y];
	}

	public boolean playerPlay(int pos) {
		if (!computerTurn) {
			for (int y = 0; y < HEIGHT; y++) {
				if (grid[pos][y] == Piece.EMPTY) {
					grid[pos][y] = Piece.PLAYER;
					computerTurn = !computerTurn;
					return true;
				}
			}
		}
		return false;
	}

	public boolean computerPlay(int pos) {
		if (computerTurn) {
			for (int y = 0; y < HEIGHT; y++) {
				if (grid[pos][y] == Piece.EMPTY) {
					grid[pos][y] = Piece.COMPUTER;
					computerTurn = !computerTurn;
					return true;
				}
			}
		}
		return false;
	}

	public boolean isComputerTurn() {
		return computerTurn;
	}

	protected boolean winForPiece(Piece piece) {
		// Lignes verticales
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT - N_IN_A_ROW_TO_WIN + 1; y++) {
				int count = 0;

				for (int line = y; line < N_IN_A_ROW_TO_WIN + y; line++) {
					if (grid[x][line] == piece) {
						count++;
					}
				}

				if (count == N_IN_A_ROW_TO_WIN) {
					return true;
				}
			}
		}

		// Lignes horizontales
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH - N_IN_A_ROW_TO_WIN + 1; x++) {
				int count = 0;

				for (int line = x; line < x + N_IN_A_ROW_TO_WIN; line++) {
					if (grid[line][y] == piece) {
						count++;
					}
				}

				if (count == N_IN_A_ROW_TO_WIN) {
					return true;
				}
			}
		}

		// Lignes diagonales "/"
		for (int x = 0; x < WIDTH - N_IN_A_ROW_TO_WIN + 1; x++) {
			for (int y = 0; y < HEIGHT - N_IN_A_ROW_TO_WIN + 1; y++) {
				int count = 0;

				for (int n = 0; n < N_IN_A_ROW_TO_WIN; n++) {
					if (grid[x + n][y + n] == piece) {
						count++;
					}
				}

				if (count == N_IN_A_ROW_TO_WIN) {
					return true;
				}
			}
		}

		// Lignes diagonales "\"
		for (int x = N_IN_A_ROW_TO_WIN - 1; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT - N_IN_A_ROW_TO_WIN + 1; y++) {
				int count = 0;

				for (int n = 0; n < N_IN_A_ROW_TO_WIN; n++) {
					if (grid[x - n][y + n] == piece) {
						count++;
					}
				}

				if (count == N_IN_A_ROW_TO_WIN) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean playerWin() {
		return winForPiece(Piece.PLAYER);
	}

	public boolean computerWin() {
		return winForPiece(Piece.COMPUTER);
	}

	public boolean gameFull() {
		for (int x = 0; x < WIDTH; x++) {
			if (grid[x][5] == Piece.EMPTY) {
				return false;
			}
		}
		return true;
	}

	public boolean gameFinished() {
		return playerWin() || computerWin() || gameFull();
	}

	public boolean canPlacePieceAt(int x, int y) {
		return grid[x][y] == Piece.EMPTY
				&& (y == 0 || grid[x][y - 1] != Piece.EMPTY);
	}
}
