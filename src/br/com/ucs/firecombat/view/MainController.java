package br.com.ucs.firecombat.view;

import java.net.URL;
import java.util.ResourceBundle;

import br.com.ucs.firecombat.main.AppMain;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class MainController implements Initializable {

	@FXML
	private GridPane grid;

	@FXML
	private Button start;

	private AppMain app = new AppMain();

	@FXML
	void startClicked(ActionEvent event) {
		try {
			app.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		testShowImages();

		app.registerFireAddedListener(fire -> {
			System.out.println("going put a fire in x=" + fire.getX() + ",y=" + fire.getY());
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					addFireInGrid(fire.getX(), fire.getY());
				}
			});
		});

		app.registerFireRemovedListener((x, y) -> {
			System.out.println("going remove a fire in x=" + x + ",y=" + y);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					removeImageInGrid(x, y);
				}
			});
		});

	}

	private void removeImageInGrid(int x, int y) {
		ImageView whiteImageView = getWhiteImageView();
		grid.add(whiteImageView, y, x);
		System.out.println("removed fire in the grid");
	}

	private void addFireInGrid(int x, int y) {
		Image imageFire = new Image("resources/fire.jpg");
		ImageView imageView = new ImageView(imageFire);
		grid.add(imageView, y, x);
		System.out.println("put fire in the grid");
	}

	private void testShowImages() {
		Image imageVictim = new Image("resources/victim.jpg");
		Image imageRefugee = new Image("resources/refugee.jpg");
		Image imageFireFighter = new Image("resources/firefighter.jpg");
		Image imageFire = new Image("resources/fire.jpg");
		Image imageParamedics = new Image("resources/paramedics.jpg");

		Pane pane = new Pane();
		Pane pane1 = new Pane();
		Pane pane2 = new Pane();
		Pane pane3 = new Pane();
		Pane pane4 = new Pane();

		grid.add(pane, 0, 0);
		grid.add(pane1, 0, 1);
		grid.add(pane2, 0, 2);
		grid.add(pane3, 0, 3);
		grid.add(pane4, 0, 4);

		ImageView imageView = new ImageView(imageVictim);
		pane.getChildren().add(imageView);
		ImageView imgView = new ImageView(imageRefugee);
		pane1.getChildren().add(imgView);
		ImageView imgView2 = new ImageView(imageFireFighter);
		pane2.getChildren().add(imgView2);
		ImageView imgView3 = new ImageView(imageFire);
		pane3.getChildren().add(imgView3);
		ImageView imgView4 = new ImageView(imageParamedics);
		pane4.getChildren().add(imgView4);
	}
	
	public ImageView getWhiteImageView() {
		Image imageWhite = new Image("resources/white.jpg");
		ImageView imageView = new ImageView(imageWhite);
		return imageView;
	}

}
