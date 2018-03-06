package chapter9;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openimaj.audio.AudioFormat;
import org.openimaj.audio.AudioPlayer;
import org.openimaj.audio.JavaSoundAudioGrabber;
import org.openimaj.audio.SampleChunk;
import org.openimaj.audio.analysis.FourierTransform;
import org.openimaj.audio.filters.*;
import org.openimaj.audio.filters.EQFilter.EQType;
import org.openimaj.video.xuggle.XuggleAudio;
import org.openimaj.vis.audio.AudioSpectrogram;
import org.openimaj.vis.audio.AudioWaveform;
import org.openimaj.vis.general.BarVisualisation;

import core.Mess;

public class Chapter9 {
	XuggleAudio xa = null;
	EQFilter eq = null;
	Mess mess = new Mess();

	public void loadChapter9() throws MalformedURLException, InterruptedException {

		int answer = mess.basicMessage("chapter9");

		if (answer == 1) {
			fileAndURLStart();
		} else if (answer == 2) {
			audioWaveStyle();
		} else if (answer == 3) {
			barVisualisationStyle();
		} else if (answer == 4) {
			audioSpectrogramStyle();
		} else {
			inputSoundByMic();
		}
	}

	private void inputSoundByMic() throws InterruptedException {
		final AudioSpectrogram Aspec = new AudioSpectrogram(440, 600);
		Aspec.showWindow("Input sound");

		final JavaSoundAudioGrabber jsag = new JavaSoundAudioGrabber(new AudioFormat(16, 44.1, 1));
		new Thread(jsag).start();

		while (jsag.isStopped()) {
			Thread.sleep(50);
		}

		SampleChunk sc = null;
		while ((sc = jsag.nextSampleChunk()) != null) {
			Aspec.setData(sc);
		}

	}

	private void audioSpectrogramStyle() {
		// getAudioURL();
		AudioSpectrogram ASpec = new AudioSpectrogram(800, 1000);
		ASpec.showWindow("AudioSpectrogram");
		eqFilterStyle();
		final FourierTransform fft = new FourierTransform(eq);
		while (fft.nextSampleChunk() != null) {
			final float[][] ffts = fft.getNormalisedMagnitudes(1f / Integer.MAX_VALUE);
			ASpec.setData(ffts[0]);
		}
	}

	private void barVisualisationStyle() {
		getAudioURL();
		BarVisualisation barVis = new BarVisualisation(800, 800);
		barVis.showWindow("barVis");
		final FourierTransform fft = new FourierTransform(xa);
		barVis.getAxesRenderer().setDrawYTicks(false);
		while (fft.nextSampleChunk() != null) {
			final float[][] ffts = fft.getNormalisedMagnitudes(1f / Integer.MAX_VALUE);
			barVis.setData(ffts[0]);
		}
	}

	private void audioWaveStyle() {
		final AudioWaveform vis = new AudioWaveform(400, 400);
		vis.showWindow("WaveForm");
		getAudioURL();
		SampleChunk sc = null;
		while ((sc = xa.nextSampleChunk()) != null) {
			vis.setData(sc.getSampleBuffer());

		}

	}

	private void fileAndURLStart() throws MalformedURLException {
		XuggleAudio xa = new XuggleAudio(new File("C:/Users/Nicklas/Workspace SilverSpin/Bilder/music/music.mp3"));
		XuggleAudio xa2 = new XuggleAudio(new URL("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"));
		AudioPlayer.createAudioPlayer(xa).run();
		AudioPlayer.createAudioPlayer(xa2).run();

	}

	/**
	 * Open a URL to the sine wave sweep.
	 * 
	 * @throws MalformedURLException
	 */
	private void getAudioURL() {
		try {
			xa = new XuggleAudio(
					new URL("http://www.audiocheck.net/download.php?filename=Audio/audiocheck.net_sweep20-20klin.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void eqFilterStyle() {
		getAudioURL();
		eq = new EQFilter(xa, EQType.LPF, 5000);

	}
}
