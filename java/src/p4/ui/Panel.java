package p4.ui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import p4.Engine;
import p4.Grid;
import p4.Piece;
import p4.engines.RandomEngine;

public class Panel extends Group {
	private final GraphicsContext gc;
	private Engine engine = new RandomEngine();

	public Panel() {
		Canvas canvas = new Canvas(1280, 720);
		this.getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();
		
		Rectangle buttons[] = new Rectangle[7];
		
		for(int n = 0; n < buttons.length; n++) {
			buttons[n] = new Rectangle();
			
			final int pos = n;
			buttons[n].setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					Window.grid.playerPlay(pos);
					update(Window.grid);
					
					Window.grid.computerPlay(engine.play(Window.grid));
					update(Window.grid);
				}
			});
			
			buttons[n].setHeight(50);
			buttons[n].setWidth(75);
			buttons[n].setFill(Color.BLACK);
			buttons[n].setX(100 + 100 * n);
			buttons[n].setY(0);
			
			this.getChildren().add(buttons[n]);
		}
	}

	public void update(Grid grid) {
		gc.setFill(Color.GRAY);
		gc.fillRect(0, 0, 1280, 720);
		for(int x = 0; x < Grid.WIDTH; x++) {
			for(int y = 0; y < Grid.HEIGHT; y++) {
				Piece piece = grid.get(x, 5 - y);
				
				if(piece == Piece.COMPUTER) {
					gc.setFill(Color.RED);
				} else if(piece == Piece.PLAYER) {
					gc.setFill(Color.BLUE);
				} else {
					gc.setFill(Color.WHITE);
				}
				
				gc.fillOval(100 + x * 100, 100 + y * 100, 75, 75);
			}
		}
	}
}
