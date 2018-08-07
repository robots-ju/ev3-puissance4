package p4;

import javafx.application.Application;
import p4.ui.Window;
import p4.vision.GridCamera;

public class Main {
	public static GridCamera camera;
	
	public static void main(String args[]) {
		RobotController.connect();
		camera = new GridCamera();
		Thread thread = new Thread(camera);
		thread.start();

		Application.launch(Window.class);
	}
}
