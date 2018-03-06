package chapter12;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.oc.ocvolume.dsp.featureExtraction;
import org.openimaj.data.DataSource;
import org.openimaj.data.dataset.Dataset;
import org.openimaj.data.dataset.GroupedDataset;
import org.openimaj.data.dataset.ListDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.experiment.dataset.sampling.GroupSampler;
import org.openimaj.experiment.dataset.sampling.GroupedUniformRandomisedSampler;
import org.openimaj.experiment.dataset.split.GroupedRandomSplitter;
import org.openimaj.experiment.evaluation.classification.ClassificationEvaluator;
import org.openimaj.experiment.evaluation.classification.ClassificationResult;
import org.openimaj.experiment.evaluation.classification.analysers.confusionmatrix.CMAnalyser;
import org.openimaj.experiment.evaluation.classification.analysers.confusionmatrix.CMResult;
import org.openimaj.feature.DiskCachingFeatureExtractor;
import org.openimaj.feature.DoubleFV;
import org.openimaj.feature.FeatureExtractor;
import org.openimaj.feature.SparseIntFV;
import org.openimaj.feature.local.data.LocalFeatureListDataSource;
import org.openimaj.feature.local.list.LocalFeatureList;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101;
import org.openimaj.image.annotation.evaluation.datasets.Caltech101.Record;
import org.openimaj.image.feature.dense.gradient.dsift.ByteDSIFTKeypoint;
import org.openimaj.image.feature.dense.gradient.dsift.DenseSIFT;
import org.openimaj.image.feature.dense.gradient.dsift.PyramidDenseSIFT;
import org.openimaj.image.feature.local.aggregate.BagOfVisualWords;
import org.openimaj.image.feature.local.aggregate.BlockSpatialAggregator;
import org.openimaj.ml.annotation.linear.LiblinearAnnotator;
import org.openimaj.ml.annotation.linear.LiblinearAnnotator.Mode;
import org.openimaj.ml.clustering.ByteCentroidsResult;
import org.openimaj.ml.clustering.assignment.HardAssigner;
import org.openimaj.ml.clustering.kmeans.ByteKMeans;
import org.openimaj.ml.kernel.HomogeneousKernelMap;
import org.openimaj.util.pair.IntFloatPair;

import de.bwaldvogel.liblinear.SolverType;

public class Chapter12 {

	public void loadChapter12() throws IOException {
		
		classificationWithCaltech101();
		
	}

	private void classificationWithCaltech101() throws IOException {
		GroupedDataset<String, VFSListDataset<Record<FImage>>, Record<FImage>> alldata = Caltech101.getData(ImageUtilities.FIMAGE_READER);
		GroupedDataset<String, ListDataset<Record<FImage>>,Record<FImage>> data = GroupSampler.sample(alldata,5, false);
		
		GroupedRandomSplitter<String, Record<FImage>> splits = new GroupedRandomSplitter<String, Record<FImage>>(data,15, 0, 15);
				
		DenseSIFT dsift = new DenseSIFT(5,7);
		PyramidDenseSIFT<FImage>pdsift = new PyramidDenseSIFT<>(dsift, 6f, 7);
		
		HardAssigner<byte[], float[], IntFloatPair> assigner = trainQuantiser(GroupedUniformRandomisedSampler.sample(splits.getTrainingDataset(), 30), pdsift);
		FeatureExtractor<DoubleFV, Record<FImage>>extractor = new PHowExtractor(pdsift, assigner);
		
		LiblinearAnnotator<Record<FImage>, String>ann = new LiblinearAnnotator<Record<FImage>, String>(extractor,Mode.MULTICLASS,SolverType.L2R_L2LOSS_SVC,1.0,0.00001);
		ann.train(splits.getTrainingDataset());
		
		ClassificationEvaluator<CMResult<String>,String, Record<FImage>>eval = 
				new ClassificationEvaluator<CMResult<String>, String, Record<FImage>>(ann, splits.getTestDataset(), 
						new CMAnalyser<Record<FImage>,String>(CMAnalyser.Strategy.SINGLE));
		
	Map<Record<FImage>, ClassificationResult<String>>guesses = eval.evaluate();
	CMResult<String>result = eval.analyse(guesses);
		
		
	
	
	
	}

	static HardAssigner<byte[], float[], IntFloatPair> trainQuantiser(
			GroupedDataset<String, ListDataset<Record<FImage>>, Record<FImage>> sample, PyramidDenseSIFT<FImage> pdsift)
	{
		List<LocalFeatureList<ByteDSIFTKeypoint>> allKeys = new ArrayList<LocalFeatureList<ByteDSIFTKeypoint>>();
		
		for (Record<FImage> rec : sample) {
			FImage img = rec.getImage();
			pdsift.analyseImage(img);
			allKeys.add(pdsift.getByteKeypoints(0.005f));
		}
		
		if (allKeys.size()>10000) {
			allKeys.subList(0, 10000);
		}
		
		ByteKMeans km = ByteKMeans.createKDTreeEnsemble(300);
		DataSource<byte[]>datasource = new LocalFeatureListDataSource<ByteDSIFTKeypoint,byte[]>(allKeys);
		ByteCentroidsResult result = km.cluster(datasource);
		
		return result.defaultHardAssigner();
		
	}
	
	static class PHowExtractor implements FeatureExtractor<DoubleFV, Record<FImage>> {
		PyramidDenseSIFT<FImage> pdsift;
		HardAssigner<byte[], float[], IntFloatPair> assigner;
		
		
		public PHowExtractor(PyramidDenseSIFT<FImage> pdsift,HardAssigner<byte[], float[], IntFloatPair>assigner){
			this.pdsift = pdsift;
			this.assigner = assigner;
		}
		
		@Override
		public DoubleFV extractFeature(Record<FImage> object) {
			FImage image = object.getImage();
			pdsift.analyseImage(image);
			BagOfVisualWords<byte[]> bovw = new BagOfVisualWords<>(assigner);
			BlockSpatialAggregator<byte[], SparseIntFV>spatial = new BlockSpatialAggregator<byte[],SparseIntFV>(bovw,2,2);
			
			return spatial.aggregate(pdsift.getByteKeypoints(0.15f), image.getBounds()).normaliseFV();
		}
		
	}
	
}
