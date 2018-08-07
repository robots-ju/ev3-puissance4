package p4;

public class Grid {
	public static final int WIDTH = 7;
	public static final int HEIGHT = 6;
	
	public static final int LINE_SIZE = 4;
	
	private Piece grid[][] = new Piece[WIDTH][HEIGHT];
	private boolean computerTurn = false;

	public Grid() {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				grid[x][y] = Piece.EMPTY;
			}
		}
	}
	
	public Piece get(int x, int y) {
		return grid[x][y];
	}
	
	public boolean playerPlay(int pos) {
		if(!computerTurn) {
			for(int y = 0; y < HEIGHT; y++) {
				if(grid[pos][y] == Piece.EMPTY) {
					grid[pos][y] = Piece.PLAYER;
					computerTurn = !computerTurn;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean computerPlay(int pos) {
		if(computerTurn) {
			for(int y = 0; y < HEIGHT; y++) {
				if(grid[pos][y] == Piece.EMPTY) {
					grid[pos][y] = Piece.COMPUTER;
					computerTurn = !computerTurn;
					return true;
				}
			}
		} 
		return false;
	}
	
	public boolean playerWin() {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT - LINE_SIZE + 1; y++) {
				int count = 0;
				for(int line = y; line < LINE_SIZE + y; line++) {
					if(grid[x][line] == Piece.PLAYER) {
						count++;
					}
				}
				if(count == LINE_SIZE) {
					return true;
				}
			}
		}
		
		for(int y = 0; y < HEIGHT; y++) {
			for(int x = 0; x < WIDTH - LINE_SIZE + 1; x++) {
				int count = 0;
				for(int line = x; line < x + LINE_SIZE; line++) {
					if(grid[line][y] == Piece.PLAYER) {
						count++;
					}
				}
				if(count == LINE_SIZE) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean computerWin() {
		
		return false;
	}
	
	public boolean gameFull() {
		for(int x = 0; x < WIDTH; x++) {
			if(grid[x][5] == Piece.EMPTY) {
				return false;
			}
		}
		return true;
	}
	
	public boolean gameFinished() {
		return playerWin() || computerWin() || gameFull();
	}
}
