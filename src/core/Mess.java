package core;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Mess {
	String menuAnswer;
	ChoiceBox<String> menuBox = null;

	public int basicMessage(String chapter) {
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setMinWidth(200);
		stage.setMinHeight(200);

		menuBox = new ChoiceBox<>();

		if (chapter.equalsIgnoreCase("core")) {
			menuBox = core();
		} else if (chapter.equalsIgnoreCase("chapter9")) {
			menuBox = c9();
		}

		Button okButton = new Button("Submit");
		okButton.setOnAction(e -> getChose(menuBox, stage));

		VBox layout = new VBox(10);
		layout.getChildren().addAll(menuBox, okButton);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout, 300, 300);
		stage.setScene(scene);
		stage.showAndWait();

		return getNumber();
	}

	private ChoiceBox<String> c9() {
		menuBox.getItems().addAll("File and URL Start", "AudioWave Style", "Bar Visualisation Style",
				"Audio Spectrogram Style", "Input Sound By Mic");
		menuBox.setValue("File and URL Start");
		return menuBox;

	}

	private ChoiceBox<String> core() {
		menuBox.getItems().addAll("Processing image", "Clustering", "Global image features", "Image matching",
				"Image Datasets", "Processing video", "Finding faces", "Getting Audio", "Feature Extraction from Audio",
				"Twitter Streams", "Classification", "Eigenfaces", "Parallel Processing");
		menuBox.setValue("Processing image");
		return menuBox;
	}

	private void getChose(ChoiceBox<String> menuBox, Stage stage) {
		menuAnswer = menuBox.getValue();
		stage.close();
	}

	private int getNumber() {

		if (menuAnswer.equalsIgnoreCase("Processing image") 
				|| menuAnswer.equalsIgnoreCase("File and URL Start")) {
			return 1;
		} else if (menuAnswer.equalsIgnoreCase("Clustering") 
				|| menuAnswer.equalsIgnoreCase("AudioWave Style")) {
			return 2;
		} else if (menuAnswer.equalsIgnoreCase("Global image features")
				|| menuAnswer.equalsIgnoreCase("Bar Visualisation Style")) {
			return 3;
		} else if (menuAnswer.equalsIgnoreCase("Image matching")
				|| menuAnswer.equalsIgnoreCase("Audio Spectrogram Style")) {
			return 4;
		} else if (menuAnswer.equalsIgnoreCase("Image Datasets") 
				|| menuAnswer.equalsIgnoreCase("Input Sound By Mic")) {
			return 5;
		} else if (menuAnswer.equalsIgnoreCase("Processing video")) {
			return 6;
		} else if (menuAnswer.equalsIgnoreCase("Finding faces")) {
			return 7;
		} else if (menuAnswer.equalsIgnoreCase("Getting Audio")) {
			return 8;
		} else if (menuAnswer.equalsIgnoreCase("Feature Extraction from Audio")) {
			return 9;
		} else if (menuAnswer.equalsIgnoreCase("Twitter Streams")) {
			return 10;
		} else if (menuAnswer.equalsIgnoreCase("Classification")) {
			return 11;
		} else if (menuAnswer.equalsIgnoreCase("Eigenfaces")) {
			return 12;
		} else if (menuAnswer.equalsIgnoreCase("Parallel Processing")) {
			return 13;
		} else {
			return 0;
		}
	}

	public int chapter9() {
		// TODO Auto-generated method stub
		return 0;
	}

}
