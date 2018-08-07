package p4.ui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import p4.Engine;
import p4.Grid;
import p4.Main;
import p4.RobotController;
import p4.engines.RandomEngine;

public class Window extends Application {
	public static final Grid grid = new Grid();
	private Engine engine = new RandomEngine();
	
	
	@Override
	public void start(final Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root, 1280, 720);
		scene.setFill(Color.rgb(191, 191, 191));

		final Panel panel = new Panel();
		root.getChildren().add(panel);

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Puissance4");
		primaryStage.show();
		
		panel.update(grid);
		
		Thread t = new Thread() {
			public void run() {
				while(primaryStage.isShowing()) {
					try {
						Thread.sleep(100);
					} catch(InterruptedException e) {
						
					}
					
					boolean changes[][] = Main.camera.gridStatus();
					
					int changesCount = 0;
					
					int pos = 0;
					
					for(int x = 0; x < Grid.WIDTH; x++) {
						for(int y = 0; y < Grid.HEIGHT; y++) {
							if(changes[x][y]) {
								pos = x;

								changesCount++;
							}
						}
					}
					
					if(changesCount > 1) {
						System.out.println("Erreur plusieurs points détectés");
					} else if(changesCount == 1) {
						Window.grid.playerPlay(pos);
						panel.update(grid);
						
						int computerMove = engine.play(grid);
						
						
						RobotController.playAt(computerMove);
						grid.computerPlay(computerMove);
						panel.update(grid);
						Main.camera.takeReferencePicture();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				System.exit(1);
			}
		};
		
		t.start();
	}
}
