package chapter4;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.pixel.statistics.HistogramModel;
import org.openimaj.math.statistics.distribution.MultidimensionalHistogram;

public class Chapter4 {

	public void loadChapter4() throws IOException {

		URL[] imageURLs = new URL[] { new URL("http://users.ecs.soton.ac.uk/dpd/projects/openimaj/tutorial/hist1.jpg"),
				new URL("http://users.ecs.soton.ac.uk/dpd/projects/openimaj/tutorial/hist2.jpg"),
				new URL("http://users.ecs.soton.ac.uk/dpd/projects/openimaj/tutorial/hist3.jpg") };
		final List<MultidimensionalHistogram> histograms = new ArrayList<MultidimensionalHistogram>();
		final HistogramModel model = new HistogramModel(4, 4, 4);

		for (URL u : imageURLs) {
			model.estimateModel(ImageUtilities.readMBF(u));
			histograms.add(model.histogram.clone());
		}
		double temp = 0;
		double distance = 0;
		ArrayList<MultidimensionalHistogram> tempHstogram = null;
		for (int i = 0; i < histograms.size(); i++) {
			for (int j = i; j < histograms.size(); j++) {
				distance = histograms.get(i).compare(histograms.get(j), DoubleFVComparison.EUCLIDEAN);
				//distance = histograms.get(i).compare(histograms.get(j), DoubleFVComparison.INTERSECTION);
				if (distance > temp) {
					tempHstogram = new ArrayList<MultidimensionalHistogram>();
					tempHstogram.add(histograms.get(i));
					tempHstogram.add(histograms.get(j));
					temp = distance;
				}
			}
			System.out.println(distance);
		}
		
		//kan inte displaya bilden
		System.out.println("**********************");
		for (MultidimensionalHistogram multidimensionalHistogram : tempHstogram) {
			
			
			
			System.out.println(multidimensionalHistogram);
		}
		System.out.println("**********************");
		
		System.out.println(temp);
	}
}
