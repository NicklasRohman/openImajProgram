package chapter8;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import javax.swing.JFrame;

import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.math.geometry.shape.Rectangle;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

public class Chapter8 extends JFrame implements Runnable, WebcamPanel.Painter {

	Dimension dim = new Dimension(1280,720);
	private static final long serialVersionUID = 1L;

	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
	private static final HaarCascadeDetector detector = new HaarCascadeDetector();
	private static final Stroke STROKE = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f,
			new float[] { 5.0f }, 0.0f);

	private Webcam webcam = null;
	private WebcamPanel.Painter painter = null;
	private List<DetectedFace> faces = null;

	public Chapter8() throws IOException {
		
		webcam = Webcam.getDefault();
		webcam.setCustomViewSizes(dim);
		webcam.open(true);

		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setPreferredSize(dim);
		panel.setPainter(this);
		panel.setFPSDisplayed(true);
		panel.setFPSLimited(true);
		panel.setPainter(this);
		panel.start();

		painter = panel.getDefaultPainter();

		add(panel);
		setTitle("Face Detector");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		EXECUTOR.execute(this);
	}

	@Override
	public void run() {
		while (true) {
			if (!webcam.isOpen()) {
				return;
			}
			faces = detector.detectFaces(ImageUtilities.createFImage(webcam.getImage()));
		}
	}

	@Override
	public void paintPanel(WebcamPanel panel, Graphics2D g2) {
		if (painter != null) {
			painter.paintPanel(panel, g2);
		}
	}

	@Override
	public void paintImage(WebcamPanel panel, BufferedImage image, Graphics2D g2) {

		if (painter != null) {
			painter.paintImage(panel, image, g2);
		}

		if (faces == null) {
			return;
		}

		Iterator<DetectedFace> detectFace = faces.iterator();
		while (detectFace.hasNext()) {

			DetectedFace face = detectFace.next();
			Rectangle bounds = face.getBounds();
			int dx = (int) (0.1 * bounds.width);
			int dy = (int) (0.2 * bounds.height);
			int x = (int) bounds.x - dx;
			int y = (int) bounds.y - dy;
			int w = (int) bounds.width  + dx;
			int h = (int) bounds.height + dy;

			g2.setStroke(STROKE);
			g2.setColor(Color.GREEN);
			g2.drawRect(x*2, y*2, w*2, h*2);
		}
	}

	public static void loadChapter8() throws IOException {
		new Chapter8();
	}
}