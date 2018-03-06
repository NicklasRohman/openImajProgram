package chapter10;


import org.openimaj.audio.AudioFormat;
import org.openimaj.audio.JavaSoundAudioGrabber;
import org.openimaj.audio.features.MFCC;
import org.openimaj.audio.features.SpectralFlux;
import org.openimaj.audio.processor.FixedSizeSampleAudioProcessor;
import org.openimaj.vis.general.BarVisualisation;

public class Chapter10 {

	
	public void loadChapter10() throws InterruptedException {
		// extractionFromAudio();
		 SpecialFluxStyle();
	}

	private void SpecialFluxStyle() {
		JavaSoundAudioGrabber jsag = new JavaSoundAudioGrabber(new AudioFormat(16, 44.1, 1));
		new Thread(jsag).start();
		FixedSizeSampleAudioProcessor fssap = new FixedSizeSampleAudioProcessor(jsag,441*3,441);
		SpectralFlux sf = new SpectralFlux(fssap);
		
		BarVisualisation barVis = new BarVisualisation(400,200);
		barVis.setAxisLocation(100);
		barVis.setMaxValue(0.0001);
		barVis.showWindow("SpecalFlux");
		
		while (sf.nextSampleChunk() !=null) {
			final double[][]flux = sf.getLastCalculatedFeature();
			barVis.setData(flux[0]);
		}
		
	}

	private void extractionFromAudio() throws InterruptedException {
		JavaSoundAudioGrabber jsag = new JavaSoundAudioGrabber(new AudioFormat(16, 44.1, 1));
		new Thread(jsag).start();
		FixedSizeSampleAudioProcessor fssap = new FixedSizeSampleAudioProcessor(jsag,441*3,441);
		
		
		BarVisualisation barVis = new BarVisualisation(400, 200);
		barVis.setAxisLocation(100);
		barVis.showWindow("extractionFromAdio");
		
		MFCC mfcc = new MFCC(fssap);

		while (mfcc.nextSampleChunk()!=null) {
			final double[][]mfccs = mfcc.getLastCalculatedFeatureWithoutFirst();
			barVis.setData(mfccs[0]);		
		}
	}	
}
