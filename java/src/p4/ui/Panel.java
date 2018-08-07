package p4.ui;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import p4.Grid;
import p4.Main;
import p4.Piece;

public class Panel extends Group {
	private final GraphicsContext gc;

	public Panel() {
		Canvas canvas = new Canvas(1280, 720);
		this.getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();
		
		Rectangle frameReset = new Rectangle(1200, 100, 50, 50);
		frameReset.setFill(Color.RED);
		
		frameReset.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				Main.camera.takeReferencePicture();
			}
		});
		
		this.getChildren().add(frameReset);
	}

	public void update(Grid grid) {
		Platform.runLater(new Runnable() {
			public void run() {
				gc.setFill(Color.GRAY);
				gc.fillRect(0, 0, 1280, 720);
				for(int x = 0; x < Grid.WIDTH; x++) {
					for(int y = 0; y < Grid.HEIGHT; y++) {
						Piece piece = grid.get(x, Grid.HEIGHT - 1 - y);
						
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
		});
	}
}
