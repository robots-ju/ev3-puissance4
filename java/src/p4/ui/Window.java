package p4.ui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import p4.Grid;

public class Window extends Application {
	public static final Grid grid = new Grid();

	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root, 1280, 720);
		scene.setFill(Color.rgb(191, 191, 191));

		Panel panel = new Panel();
		root.getChildren().add(panel);

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Puissance4");
		primaryStage.show();
		
		panel.update(grid);
	}
}
