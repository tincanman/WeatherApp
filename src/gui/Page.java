package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public abstract class Page {
	private Scene pageScene;
	private VBox pageContentandNav;
	protected GridPane mainContentGrid;
	private String name;
	protected Button lButton;
	protected Button rButton;
	protected String displayName;
	private HBox navBar;
	
	protected static final int BTN_WIDTH = 320;
	protected static final int BTN_HEIGHT = 108;

	public Page(String name, String displayName, String leftButtonText, String rightButtonText) {
		this.name = name;
		this.displayName = displayName;
		
		// Set up the grids and layout managers
		pageContentandNav = new VBox();
		pageContentandNav.setPrefSize(320, 480);
		mainContentGrid = new GridPane();
		mainContentGrid.setPrefSize(320, 432);
		navBar = new HBox();
		navBar.setPrefSize(320, 48);
		
		// Create the main content
		createContent();
		
		// Create the nav bar
		lButton = new Button(leftButtonText);
		lButton.setPrefSize(160, 48);
		lButton.setId("navButton");
		lButton.setBorder(new Border(new BorderStroke(
				Color.web("#7d7d7d"), 
				BorderStrokeStyle.SOLID,
				CornerRadii.EMPTY,
				new BorderWidths(0, 1, 0, 0))));
		lButton.setOnAction(e -> leftButtonAction());
		
		rButton = new Button(rightButtonText);
		rButton.setPrefSize(160, 48);
		rButton.setId("navButton");
		rButton.setBorder(new Border(new BorderStroke(
				Color.web("#7d7d7d"), 
				BorderStrokeStyle.SOLID,
				CornerRadii.EMPTY,
				new BorderWidths(0, 0, 0, 1))));
		rButton.setOnAction(e -> rightButtonAction());
		
		navBar.getChildren().addAll(lButton, rButton);
		
		// Add main content and navbar to the scene and generate the scene
		pageContentandNav.getChildren().addAll(mainContentGrid, navBar);
		
		// Create menubar
		MenuBar menuBar = new MenuBar();
    	menuBar.setPrefHeight(32);
        Menu menuVers = new Menu("Version");
        MenuItem ipad = new MenuItem("iPad");
        ipad.setOnAction(e -> {
        	WeatherApp.isIphone = false;
        	WeatherApp.changePage(WeatherApp.currentPageName);
        });
        MenuItem iphone = new MenuItem("iPhone");
        iphone.setOnAction(e -> {
        	WeatherApp.isIphone = true;
        	WeatherApp.changePage(WeatherApp.currentPageName);
        });
        menuVers.getItems().addAll(ipad, iphone);
        menuBar.getMenus().add(menuVers);
		
		pageScene = new Scene(pageContentandNav, 320, 512);
		((VBox) pageScene.getRoot()).getChildren().add(menuBar);
		pageScene.getStylesheets().add(getClass().getResource("/home.css").toExternalForm());
	}

	public String getName() {
		return name;
	}

	public Scene getScene() {
		return pageScene;
	}
	
	abstract void leftButtonAction();
	
	abstract void rightButtonAction();

	abstract void createContent();

	public void refreshPage() {
		createContent();
	};
}
