package chapter13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs2.FileSystemException;
import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.dataset.util.DatasetAdaptors;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.DoubleFVComparison;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.model.EigenImages;

public class Chapter13 {

	public void loadChapter13() throws FileSystemException {

		eigenfacesSetup();

	}

	private void eigenfacesSetup() throws FileSystemException {
		int nTraining = 5;
		int nTester = 5;
		
		/*
		 * Load the data, and create some training and test data
		 */
		final VFSGroupDataset<FImage> dataset = new VFSGroupDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip",
				ImageUtilities.FIMAGE_READER);

		final GroupedRandomSplitter<String, FImage> splits = new GroupedRandomSplitter<String, FImage>(dataset, nTraining, 0,
				nTester);
		final GroupedDataset<String, ListDataset<FImage>, FImage> training = splits.getTrainingDataset();
		final GroupedDataset<String, ListDataset<FImage>, FImage> testing = splits.getTestDataset();

		/*
		 * Now learn the PCA basis
		 */
		
		final List<FImage> basesImage = DatasetAdaptors.asList(training);
		final int nEigenvectors = 100;
		final EigenImages eigen = new EigenImages(nEigenvectors);
		eigen.train(basesImage);

		/*
		 * Display the top 12 eigenimages
		 */
		List<FImage> eigenFaces = new ArrayList<FImage>();
		for (int i = 0; i < 12; i++) {
			eigenFaces.add(eigen.visualisePC(i));
		}
		DisplayUtilities.display("EigenFaces", eigenFaces);

		/*
		 * Build a map of person ->[features] for all the training data
		 */
		final Map<String, DoubleFV[]> features = new HashMap<String, DoubleFV[]>();
		for (final String person : training.getGroups()) {
			final DoubleFV[] fvs = new DoubleFV[nTraining];

			for (int i = 0; i < nTraining; i++) {
				final FImage face = training.get(person).get(i);
				fvs[i] = eigen.extractFeature(face);
			}
			features.put(person, fvs);
		}

		/*
		 * Now we can test our performance on the test set
		 */
		double correct = 0, incorrect = 0;
		for (final String truePerson : testing.getGroups()) {
			for (final FImage face : testing.get(truePerson)) {
				final DoubleFV testFeature = eigen.extractFeature(face);

				String bestPerson = null;
				Double minDistanse = Double.MAX_VALUE;
				
				for (final String person : features.keySet()) {
					for (final DoubleFV fv : features.get(person)) {
						double distance = fv.compare(testFeature, DoubleFVComparison.EUCLIDEAN);
						if (distance < minDistanse) {
							minDistanse = distance;
							bestPerson = person;
						}
					}
				}
				System.out.println("Actual: " + truePerson + "\tguess: " + bestPerson);
				if (truePerson.equals(bestPerson)) {
					correct++;
				}
				else {
					incorrect++;
				}
			}
		}
		System.out.println("Accurcy: " + (correct /(correct+incorrect))*100 +"%");
	}
}
