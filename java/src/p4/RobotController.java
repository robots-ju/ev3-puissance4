package p4;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class RobotController {
	public static final String IP = "192.168.2.70";
	public static final int PORT = 8953;

	private static Socket socket;

	public static void connect() {
		try {
			socket = new Socket(IP, PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void playAt(int pos) {
		try {
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();

			os.write(pos);

			System.out.println("Mouvement envoyé");

			is.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
