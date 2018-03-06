package chapter3;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openimaj.image.*;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.connectedcomponent.GreyscaleConnectedComponentLabeler;
import org.openimaj.image.pixel.ConnectedComponent;
import org.openimaj.image.typography.hershey.HersheyFont;
import org.openimaj.ml.clustering.FloatCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.FloatKMeans;

import cern.colt.Arrays;


public class Chapter3 {

	public void loadChapter3() throws IOException {

		MBFImage image = ImageUtilities.readMBF(new File("apa.jpg"));
		image = ColourSpace.convert(image,ColourSpace.CIE_Lab);
		FloatKMeans cluster = FloatKMeans.createExact(2);
		
		float[][] imageData = image.getPixelVectorNative(new float[(image.getWidth()*image.getHeight())][3]);
		FloatCentroidsResult result = cluster.cluster(imageData);
		float[][] centroids = result.centroids;
		
		for (float[] fs : centroids) {
			System.out.println(Arrays.toString(fs));
		}
		HardAssigner<float[], ?,?>assigner = result.defaultHardAssigner();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				float[]pixel = image.getPixelNative(x,y);
				int centroid = assigner.assign(pixel);
				image.setPixelNative(x, y, centroids[centroid]);
			}
		}
		
		image = ColourSpace.convert(image, ColourSpace.RGB);
		DisplayUtilities.display(image);
		
		GreyscaleConnectedComponentLabeler labeler = new GreyscaleConnectedComponentLabeler();
		List<ConnectedComponent>components = labeler.findComponents(image.flatten());
		
		int i = 0;
		for (ConnectedComponent comp : components) {
			if (comp.calculateArea()<50) {
				continue;
			}
			image.drawText("Point:" + (i++), comp.calculateCentroidPixel(), HersheyFont.TIMES_MEDIUM, 20,RGBColour.BLUE);
		}
		
		DisplayUtilities.display(image).setLocation(500, 0);;
		
		
	}
}
