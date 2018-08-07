package p4.engines;

import java.util.Random;

import p4.Engine;
import p4.Grid;
import p4.Piece;

public class RandomEngine implements Engine {
	private static final Random rand = new Random();

	@Override
	public int play(Grid grid) {
		int possibles = 0;
		for(int x = 0; x < Grid.WIDTH; x++) {
			if(grid.get(x, 5) == Piece.EMPTY) {
				possibles++;
			}
		}
		
		if(possibles == 0) {
			throw new RuntimeException("Aucun coup possible");
		}
		
		int position = rand.nextInt(possibles);
		
		for(int x = 0; x < Grid.WIDTH; x++) {
			if(grid.get(x, 5) == Piece.EMPTY) {
				if(position == 0) {
					return x;
				}
				position--;
			}
		}
		throw new RuntimeException("Aucun coup possible");
	}

}
