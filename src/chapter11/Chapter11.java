package chapter11;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.List;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.FaceDetector;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.stream.functions.ImageFromURL;
import org.openimaj.stream.functions.ImageSiteURLExtractor;
import org.openimaj.stream.functions.twitter.TwitterURLExtractor;
import org.openimaj.stream.provider.twitter.TwitterStreamDataset;
import org.openimaj.util.api.auth.DefaultTokenFactory;
import org.openimaj.util.api.auth.common.TwitterAPIToken;
import org.openimaj.util.function.MultiFunction;
import org.openimaj.util.function.Operation;
import org.openimaj.util.stream.Stream;

import twitter4j.Status;


public class Chapter11 {

	public void loadChapter11() {
		twitterStream();
	}

	private void twitterStream() {
		/*
		 * Construct a twitter stream with an
		 */
		final TwitterAPIToken token = DefaultTokenFactory.get(TwitterAPIToken.class);
		final Stream<Status> stream = new TwitterStreamDataset(token);

		// Get the URLs
		final Stream<URL> urlStream = stream.map(new TwitterURLExtractor()); 
		// Transform/filter to get potential image URLs
		final Stream<URL> imageUrlStream = urlStream.map(new ImageSiteURLExtractor(false));
		
		final Stream<MBFImage> imageStream = imageUrlStream.map(ImageFromURL.MBFIMAGE_EXTRACTOR);
		
		imageStream.map(new MultiFunction<MBFImage,MBFImage>() {
		HaarCascadeDetector detector = HaarCascadeDetector.BuiltInCascade.frontalface_default.load();

		@Override
		public List<MBFImage> apply(MBFImage in) {
			final List<DetectedFace>detected = detector.detectFaces(in.flatten());
			final List<MBFImage> faces = new ArrayList<MBFImage>();
			for (final DetectedFace face : detected) {
				faces.add(in.extractROI(face.getBounds()));
			}

			return faces;
			
			
		}
		}).forEach(new Operation<MBFImage>() {

			@Override
			public void perform(MBFImage image) {
				DisplayUtilities.displayName(image, "image",true);
				
			}
		});
		
	}
}
