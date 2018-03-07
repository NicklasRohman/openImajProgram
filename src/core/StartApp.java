package core;

import org.openimaj.image.processing.face.detection.FaceDetector;

import chapter10.Chapter10;
import chapter11.Chapter11;
import chapter12.Chapter12;
import chapter13.Chapter13;
import chapter14.Chapter14;
import chapter2.Chapter2LoadImage;
import chapter3.Chapter3;
import chapter4.Chapter4;
import chapter5.Chapter5;
import chapter6.Chapter6;
import chapter7.Chapter7;
import chapter8.Chapter8;
import chapter9.Chapter9;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartApp extends Application  {

	public static void main(String[] args)  throws Exception  {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		Mess mess = new Mess();
		int answer = mess.basicMessage("core");
		System.out.println("chapter: "+(answer+1));

		switch (answer) {
		case 0:
			System.out.println("Program is comming");
			break;
		
		case 1:
			Chapter2LoadImage c2 = new Chapter2LoadImage();
			c2.loadImage();
			break;
		
		case 2:
			Chapter3 c3 = new Chapter3();
			c3.loadChapter3();
			break;

		case 3:
			Chapter4 c4 = new Chapter4();
			c4.loadChapter4(); // ingen bild
			break;
		
		case 4:
			Chapter5 c5 = new Chapter5();
			c5.loadChapter5();
			/*
			 * Failed to load implementation from:
			 * com.github.fommil.netlib.NativeSystemLAPACK
			 */
			break;
		
		case 5:
			Chapter6 c6 = new Chapter6();
			c6.loadChapter6();
			break;
		
		case 6:
			Chapter7 c7 = new Chapter7();
			c7.loadChapter7();

			break;
		case 7:
			Chapter8.loadChapter8();
			
			break;
		case 8:
			Chapter9 c9 = new Chapter9();
			c9.loadChapter9();
			break;

		case 9:
			Chapter10 c10 = new Chapter10();
			c10.loadChapter10();
			break;

		case 10:
			Chapter11 c11 = new Chapter11();
			c11.loadChapter11();
			break;

		case 11:
			Chapter12 c12 = new Chapter12();
			c12.loadChapter12();
			break;

		case 12:
			Chapter13 c13 = new Chapter13();
			c13.loadChapter13();
			break;

		case 13:
			Chapter14 c14 = new Chapter14();
			c14.loadChapter();
			break;

		default:
			break;
		}
}
}
