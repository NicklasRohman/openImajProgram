package chapter6;

import java.util.Map.Entry;

import org.openimaj.data.dataset.VFSGroupDataset;
import org.openimaj.data.dataset.VFSListDataset;
import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.dataset.FlickrImageDataset;
import org.openimaj.util.api.auth.DefaultTokenFactory;
import org.openimaj.util.api.auth.common.FlickrAPIToken;
public class Chapter6 {

	public void loadChapter6() throws Exception {
		VFSListDataset<FImage> images = new VFSListDataset<FImage>("C:/Users/Nicklas/Workspace SilverSpin/Bilder", ImageUtilities.FIMAGE_READER);
		System.out.println(images.size());
		
		DisplayUtilities.display(images.getRandomInstance(),"A random Image").setLocation(0, 0);;
		
		DisplayUtilities.display("all Images", images).setLocation(0, 500);;
		
		VFSListDataset<FImage>faces = new VFSListDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip",	ImageUtilities.FIMAGE_READER);
		
		DisplayUtilities.display("Zipfile whit faces",faces);
		
		final VFSGroupDataset<FImage>groupedFaces = new VFSGroupDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip",ImageUtilities.FIMAGE_READER);
		for (final Entry<String,VFSListDataset<FImage>> entry : groupedFaces.entrySet()) {
			DisplayUtilities.display(entry.getKey(),entry.getValue()).setLocation(1000, 500);;
		}

//		final FlickrAPIToken flickrToken = DefaultTokenFactory.get(FlickrAPIToken.class);
//		FlickrImageDataset<FImage> cats = FlickrImageDataset.create(ImageUtilities.FIMAGE_READER, flickrToken,"cat", 10);
//		DisplayUtilities.display("Cats", cats);

		
		//*****Exercise 1*****
		
		final VFSGroupDataset<FImage>groupedFaces2 = new VFSGroupDataset<FImage>("zip:http://datasets.openimaj.org/att_faces.zip",ImageUtilities.FIMAGE_READER);
		for (final Entry<String,VFSListDataset<FImage>> entry : groupedFaces2.entrySet()) {
			DisplayUtilities.display(entry.getKey(),entry.getValue().getRandomInstance()).setLocation(1000, 0);;
		}
		
		
		
		
		
	}

}
