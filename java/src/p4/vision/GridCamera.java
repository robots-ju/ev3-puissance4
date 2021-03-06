package p4.vision;

import static org.bytedeco.javacpp.opencv_core.cvAbsDiff;
import static org.bytedeco.javacpp.opencv_core.cvGet2D;
import static org.bytedeco.javacpp.opencv_imgproc.putText;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;

import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import p4.Grid;
import p4.ui.Window;

public class GridCamera implements Runnable {
	final int INTERVAL = 100;
	final int ESPACEX = 83;
	final int STARTX = 95;
	final int STARTY = 60;
	final int TRESHOLD_DETECT = 120;
	final int STEP_SIZE = 2;
	final int DEVICE_NUMBER = 0;
	final int WINDOWS_SCREEN_X = 0;

	CanvasFrame canvasPrev = new CanvasFrame("Prev");
	CanvasFrame canvasDiff = new CanvasFrame("Diff");
	CanvasFrame canvasArea = new CanvasFrame("Area");

	protected boolean takeReferencePicture = false;

	protected boolean gridStatus[][] = new boolean[Grid.WIDTH][Grid.HEIGHT];

	public GridCamera() {
		canvasPrev.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		canvasPrev.setLocation(WINDOWS_SCREEN_X, 0);
		canvasDiff.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		canvasDiff.setLocation(WINDOWS_SCREEN_X, 800);
		canvasArea.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		canvasArea.setLocation(WINDOWS_SCREEN_X + 800, 300);
	}

	public void takeReferencePicture() {
		takeReferencePicture = true;
	}

	public boolean[][] gridStatus() {
		return gridStatus;
	}

	public void run() {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(DEVICE_NUMBER);
		OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
		IplImage prevImg = null;
		IplImage nextImg;
		Mat areaImg;
		IplImage diffImg;

		int ESPACEY = (int) Math.floor(ESPACEX / 1.2);

		// "rayon" du carré utilisé pour la détection
		int AVG_SQUARE_SIZE = (int) Math.floor(ESPACEX / 4);

		try {
			grabber.start();

			while (true) {
				Frame frame = grabber.grab();

				nextImg = converter.convert(frame);

				if (prevImg == null || takeReferencePicture) {
					prevImg = nextImg.clone();
					takeReferencePicture = false;
				}

				diffImg = nextImg.clone();

				cvAbsDiff(prevImg, nextImg, diffImg);

				areaImg = new Mat(nextImg.clone());

				int avgCount = 0;

				for (int gridX = 0; gridX < Grid.WIDTH; gridX++) {
					for (int gridY = 0; gridY < Grid.HEIGHT; gridY++) {
						Scalar color = Scalar.RED;
						boolean detected = false;
						double avg = 0;

						// Centre du cadre de détection
						int positionX = STARTX + gridX * ESPACEX;
						int positionY = STARTY + gridY * ESPACEY;

						// Coordonnées dans l'objet représentant la partie
						// La grille est indexée depuis le bas donc on inverse Y
						int gridReferenceX = gridX;
						int gridReferenceY = Grid.HEIGHT - 1 - gridY;

						if (Window.grid.canPlacePieceAt(gridReferenceX,
								gridReferenceY)) {

							color = Scalar.BLUE;

							int total = 0;
							avgCount = 0;

							for (int pixelX = positionX - AVG_SQUARE_SIZE; pixelX <= positionX
									+ AVG_SQUARE_SIZE; pixelX += STEP_SIZE) {

								for (int pixelY = positionY - AVG_SQUARE_SIZE; pixelY <= positionY
										+ AVG_SQUARE_SIZE; pixelY += STEP_SIZE) {
									CvScalar pixel = cvGet2D(diffImg, pixelY,
											pixelX);
									total += pixel.getVal(0) + pixel.getVal(1)
											+ pixel.getVal(2);
									avgCount++;
								}
							}

							if (avgCount > 0) {
								avg = total / avgCount;

								detected = avg > TRESHOLD_DETECT;
							}
						}

						if (detected) {
							color = Scalar.GREEN;
						}

						Point topLeft = new Point(positionX - AVG_SQUARE_SIZE,
								positionY - AVG_SQUARE_SIZE);
						Point bottomRight = new Point(positionX
								+ AVG_SQUARE_SIZE, positionY + AVG_SQUARE_SIZE);

						rectangle(areaImg, topLeft, bottomRight, color);
						putText(areaImg, Double.toString(avg), topLeft, 1, 1,
								Scalar.BLUE);

						// Place le résultat dans la grille
						// La grille de résulat est indexée depuis le bas donc
						// on inverse Y
						gridStatus[gridX][Grid.HEIGHT - 1 - gridY] = detected;
					}
				}

				putText(areaImg,
						"Echantillons par carre: " + Double.toString(avgCount),
						new Point(5, diffImg.height() - 5), 1, 1, Scalar.BLUE);

				canvasPrev.showImage(converter.convert(prevImg));
				canvasDiff.showImage(converter.convert(diffImg));
				canvasArea.showImage(converter.convert(areaImg));

				Thread.sleep(INTERVAL);
			}
		} catch (Exception e) {
			e.printStackTrace();

			System.exit(1);
		}
	}
}