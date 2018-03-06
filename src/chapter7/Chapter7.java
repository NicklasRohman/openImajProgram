package chapter7;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.edges.CannyEdgeDetector;
import org.openimaj.video.Video;
import org.openimaj.video.VideoDisplay;
import org.openimaj.video.VideoDisplayListener;
import org.openimaj.video.capture.VideoCapture;
import org.openimaj.video.capture.VideoCaptureException;
import org.openimaj.video.xuggle.XuggleVideo;

public class Chapter7 {

	public void loadChapter7() throws VideoCaptureException, MalformedURLException {

		//goForCamVideo(); // fungerar inte hittar inte dll fil
		// goForURLVideo(); // flash video fungerar inte
		 goForFileVideo();

	}

	private void goForFileVideo() {
		Video<MBFImage> FileVideo;
		FileVideo = new XuggleVideo("C:/Users/Nicklas/Workspace SilverSpin/Bilder/Video/keyboardcat.flv");
		VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(FileVideo);
		goForCannyEdgesVideo(FileVideo);
	}

	private void goForURLVideo() throws MalformedURLException {
		Video<MBFImage> URLVideo;
		URLVideo = new XuggleVideo(new URL("https://www.youtube.com/watch?v=oe5tzhEaNAk"));
		VideoDisplay<MBFImage> display3 = VideoDisplay.createVideoDisplay(URLVideo);
	}

	private void goForCamVideo() throws VideoCaptureException {
		Video<MBFImage> CamVideo;
		CamVideo = new VideoCapture(500, 500);
		VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(CamVideo);
		for (MBFImage mbfImage : CamVideo) {
			DisplayUtilities.displayName(mbfImage.process(new CannyEdgeDetector()), "VideoFrames");
		}
	}

	private void goForCannyEdgesVideo(Video<MBFImage> cannyVideo) {
		VideoDisplay<MBFImage> display = VideoDisplay.createVideoDisplay(cannyVideo);
		display.addVideoListener(new VideoDisplayListener<MBFImage>() {

			@Override
			public void beforeUpdate(MBFImage frame) {
				frame.processInplace(new CannyEdgeDetector());
			}
			@Override
			public void afterUpdate(VideoDisplay<MBFImage> display) {
			}
		});
	}
}
