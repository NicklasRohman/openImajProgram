package chapter14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.experiment.dataset.sampling.GroupSampler;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.resize.ResizeProcessor;
import org.openimaj.time.Timer;
import org.openimaj.util.function.Operation;
import org.openimaj.util.parallel.Parallel;
import org.openimaj.util.parallel.partition.RangePartitioner;

public class Chapter14 {
	int path = 2;

	public void loadChapter() throws IOException {

		// pararellCount();
		pararellImage();

	}

	private void pararellImage() throws IOException {
		VFSGroupDataset<MBFImage> allImages = Caltech101.getImages(ImageUtilities.MBFIMAGE_READER);

		GroupedDataset<String, ListDataset<MBFImage>, MBFImage> images = GroupSampler.sample(allImages, 8, false);

		final List<MBFImage> output = new ArrayList<MBFImage>();
		final ResizeProcessor resize = new ResizeProcessor(200);

		if (path == 0) {
			final Timer t1 = Timer.timer();
			for (final ListDataset<MBFImage> clzImages : images.values()) {
				final MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);

				for (final MBFImage i : clzImages) {
					final MBFImage temp = new MBFImage(200, 200, ColourSpace.RGB);
					temp.fill(RGBColour.WHITE);

					final MBFImage small = i.process(resize).normalise();
					final int x = (200 - small.getWidth() / 2);
					final int y = (200 - small.getHeight() / 2);
					temp.drawImage(small, x, y);
					current.addInplace(temp);
				}
				current.divideInplace((float) clzImages.size());
				output.add(current);
			}

			System.out.println("NonParallel Timer " + t1.duration() + "ms");
		} else if (path == 1) {

			Timer t2 = Timer.timer();
			for (ListDataset<MBFImage> clzImages : images.values()) {
				final MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);
				Parallel.forEach(clzImages, new Operation<MBFImage>() {

					@Override
					public void perform(MBFImage i) {
						final MBFImage temp = new MBFImage(200, 200, ColourSpace.RGB);
						temp.fill(RGBColour.WHITE);

						final MBFImage small = i.process(resize).normalise();
						final int x = (200 - small.getWidth() / 2);
						final int y = (200 - small.getHeight() / 2);
						temp.drawImage(small, x, y);

						synchronized (current) {
							current.addInplace(temp);
						}
					}
				});
				current.divideInplace((float) clzImages.size());
				output.add(current);
			}
			System.out.println("Parallel Timer " + t2.duration() + "ms");
		}

		else if (path == 2) {
			Timer t3 = Timer.timer();
			for (ListDataset<MBFImage> clzImages : images.values()) {
				final MBFImage current = new MBFImage(200, 200, ColourSpace.RGB);

				Parallel.forEachPartitioned(new RangePartitioner<MBFImage>(clzImages),
						new Operation<Iterator<MBFImage>>() {

							@Override
							public void perform(Iterator<MBFImage> it) {
								MBFImage tempAccum = new MBFImage(200, 200, 3);
								MBFImage temp = new MBFImage(200, 200, ColourSpace.RGB);

								while (it.hasNext()) {

									final MBFImage i = it.next();
									temp.fill(RGBColour.WHITE);

									final MBFImage small = i.process(resize).normalise();
									final int x = (200 - small.getWidth()) / 2;
									final int y = (200 - small.getHeight()) / 2;
									temp.drawImage(small, x, y);
									tempAccum.addInplace(temp);
								}
								synchronized (current) {
									current.addInplace(tempAccum);
								}

							}

						});
				current.divideInplace((float) clzImages.size());
				output.add(current);
			}
			System.out.println("Parallel Timer " + t3.duration() + "ms");
		}

		DisplayUtilities.display("images", output);
	}

	private void pararellCount() {
		Parallel.forIndex(0, 10, 1, new Operation<Integer>() {

			@Override
			public void perform(Integer i) {
				System.out.println(i);
			}
		});

	}
}
