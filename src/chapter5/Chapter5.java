package chapter5;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.feature.local.matcher.BasicMatcher;
import org.openimaj.feature.local.matcher.FastBasicKeypointMatcher;
import org.openimaj.feature.local.matcher.LocalFeatureMatcher;
import org.openimaj.feature.local.matcher.MatchingUtilities;
import org.openimaj.feature.local.matcher.consistent.ConsistentLocalFeatureMatcher2d;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.feature.local.engine.DoGSIFTEngine;
import org.openimaj.image.feature.local.keypoints.Keypoint;
import org.openimaj.math.geometry.transforms.estimation.RobustAffineTransformEstimator;
import org.openimaj.math.model.fit.RANSAC;

public class Chapter5 {

	LocalFeatureList<Keypoint>queryKeyPoint;
	LocalFeatureList<Keypoint>targetKeyPoint;
	LocalFeatureMatcher<Keypoint>matcher;
	MBFImage query;
	MBFImage target;
	
	public void loadChapter5() throws MalformedURLException, IOException{
		query = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/query.jpg"));
		target = ImageUtilities.readMBF(new URL("http://static.openimaj.org/media/tutorial/target.jpg"));

	DoGSIFTEngine engine = new DoGSIFTEngine();
	queryKeyPoint = engine.findFeatures(query.flatten());
	targetKeyPoint = engine.findFeatures(target.flatten());
	
	BasicMatchMethod();
	RobustAffineTransformEstimatorMethod();
	
	}

	private void BasicMatchMethod() {
		matcher = new BasicMatcher<Keypoint>(8);
		matcher.setModelFeatures(queryKeyPoint);
		matcher.findMatches(targetKeyPoint);
		
		MBFImage basicMatch = MatchingUtilities.drawMatches(query,target, matcher.getMatches(),RGBColour.RED);
		DisplayUtilities.display(basicMatch).setLocation(0, 0);
	}

	private void RobustAffineTransformEstimatorMethod() {
		RobustAffineTransformEstimator modelFitter = new RobustAffineTransformEstimator(5.0,1500,new RANSAC.PercentageInliersStoppingCondition(0.5));
		matcher = new ConsistentLocalFeatureMatcher2d<Keypoint>(new FastBasicKeypointMatcher<>(8),modelFitter);
		
		matcher.setModelFeatures(queryKeyPoint);
		matcher.findMatches(targetKeyPoint);
		
		MBFImage consistentMatches = MatchingUtilities.drawMatches(query, target, matcher.getMatches(),RGBColour.GREEN); 
		DisplayUtilities.display(consistentMatches).setLocation(0, 500);
		
		target.drawShape(query.getBounds().transform(modelFitter.getModel().getTransform().inverse()),RGBColour.BLUE);
		DisplayUtilities.display(target);
	}
}
