package chapter2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openimaj.image.*;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.math.geometry.shape.Ellipse;


public class Chapter2LoadImage {
	MBFImage image;
	MBFImage clone;
	MBFImage edges;
	MBFImage spechBubble;

	public void loadImage() throws IOException {
			image = ImageUtilities.readMBF(new File("apa.jpg"));
		displayImage(image);

	}

	/**
	 * Showing the image
	 * 
	 * @param image
	 */
	private void displayImage(MBFImage image) {
		List<MBFImage>imageTest= new ArrayList<>();
	
		spechBubble = drawingOnImage(image); 
		edges = showEdges(image);
		clone = makeImageRed(image);

		
		imageTest.add(image);
		imageTest.add(clone);
		imageTest.add(edges);
		imageTest.add(spechBubble);

		DisplayUtilities.display("",imageTest).setSize(1500, 500);
	}

	
	private MBFImage drawingOnImage(MBFImage image) {
		spechBubble = image.clone();
		spechBubble.drawShapeFilled(new Ellipse(300f, 125f, 70f, 50f, 0f), RGBColour.WHITE);
		spechBubble.drawShapeFilled(new Ellipse(220f, 185f, 15f, 10f, 0f), RGBColour.WHITE);
		spechBubble.drawShapeFilled(new Ellipse(200f, 210f, 5f, 5f, 0f), RGBColour.WHITE);
		spechBubble.drawText("OpenImaj is ",  240, 115, HersheyFont.ASTROLOGY, 14, RGBColour.BLACK);
		spechBubble.drawText("drawing",  260, 135, HersheyFont.ASTROLOGY, 14, RGBColour.RED);
		spechBubble.drawShape(new Ellipse(300f, 125f, 70f, 50f, 0f), 5, RGBColour.GREEN);
		
		return spechBubble;
	}

	private MBFImage showEdges(MBFImage image) {
		edges = image.clone();
		edges.processInplace(new CannyEdgeDetector());
		
		return edges;
	}

	private MBFImage makeImageRed(MBFImage image) {
		clone = image.clone();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				clone.getBand(1).pixels[y][x] = 0;
				clone.getBand(2).pixels[y][x] = 0;
			}
		}
		return clone;
	}

	
	
	
}
