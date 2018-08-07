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

public class GridCamera implements Runnable {
	final int INTERVAL = 1000;
	final int ESPACEX = 83;
	final int STARTX = 95;
	final int STARTY = 60;
	final int TRESHOLD_DETECT = 120;

	CanvasFrame canvasPrev = new CanvasFrame("Prev");
	CanvasFrame canvasDiff = new CanvasFrame("Diff");
	CanvasFrame canvasArea = new CanvasFrame("Area");

	boolean takeReferencePicture = false;

	boolean gridStatus[][] = new boolean[Grid.WIDTH][Grid.HEIGHT];

	public GridCamera() {
		canvasPrev.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		canvasDiff.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		canvasDiff.setLocation(0, 400);
		canvasArea.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		canvasArea.setLocation(700, 400);
	}

	public void takeReferencePicture() {
		takeReferencePicture = true;
	}

	public boolean[][] gridStatus() {
		return gridStatus;
	}

	public void run() {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
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
				}

				diffImg = nextImg.clone();

				cvAbsDiff(prevImg, nextImg, diffImg);

				areaImg = new Mat(nextImg.clone());

				for (int gridX = 0; gridX < Grid.WIDTH; gridX++) {
					for (int gridY = 0; gridY < Grid.HEIGHT; gridY++) {
						int positionX = STARTX + gridX * ESPACEX;
						int positionY = STARTY + gridY * ESPACEY;

						int total = 0;
						int count = 0;

						for (int pixelX = positionX - AVG_SQUARE_SIZE; pixelX <= positionX
								+ AVG_SQUARE_SIZE; pixelX++) {

							for (int pixelY = positionY - AVG_SQUARE_SIZE; pixelY <= positionY
									+ AVG_SQUARE_SIZE; pixelY++) {
								CvScalar pixel = cvGet2D(diffImg, pixelY,
										pixelX);
								total += pixel.getVal(0) + pixel.getVal(1)
										+ pixel.getVal(2);
								count++;
							}

						}

						double avg = count > 0 ? total / count : 0;

						Scalar color = Scalar.BLUE;

						boolean detected = avg > TRESHOLD_DETECT;

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