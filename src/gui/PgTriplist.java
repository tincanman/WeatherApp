package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import api.Trip;
import api.Weather;
import api.YWeatherConnection;

public class PgTriplist extends Page{
	private ScrollPane scrollPane;
	private VBox scrollContent;
	
	private class TriplistPane {
		public HBox infoPane;
		
		public TriplistPane(Trip trip) {
			// First get the weather for this trip on the day we are looking at
			Weather startWeather = YWeatherConnection.
					getWeather(trip.getStartWoeid()).get(WeatherApp.currentlyViewingDayIdx);
			Weather destWeather = YWeatherConnection.
					getWeather(trip.getDestWoeid()).get(WeatherApp.currentlyViewingDayIdx);
			
			infoPane = new HBox();
			infoPane.setAlignment(Pos.CENTER);
			
			// Start location info
			VBox startPane = new VBox();
			startPane.setPrefWidth(150);
			
			Label startName = new Label(trip.getStart());
			startName.getStyleClass().add("triplistname");
			startName.setAlignment(Pos.CENTER_LEFT);
			startPane.getChildren().add(startName);
			
			HBox startWeatherPane = new HBox();
			startWeatherPane.setAlignment(Pos.CENTER);
			
			Label startWeatherIcon = new Label(
					WeatherApp.weatherIconMap[startWeather.getCondCode()]);
			startWeatherIcon.getStyleClass().add("weathericon");
			startWeatherIcon.setId("triplistwicon");
			startWeatherIcon.setPrefWidth(75);
			startWeatherIcon.setTranslateY(-10);
			startWeatherIcon.setAlignment(Pos.CENTER);
			startWeatherPane.getChildren().add(startWeatherIcon);
			
			Label startTemp = new Label(startWeather.getCurrentTemp() + "\u2103");
			startTemp.getStyleClass().add("triplisttemp");
			startTemp.setPrefWidth(75);
			startTemp.setAlignment(Pos.CENTER_LEFT);
			startWeatherPane.getChildren().add(startTemp);
			
			startPane.getChildren().add(startWeatherPane);
			
			Label leaveTime = new Label("Leave " + trip.getLeaveTime());
			leaveTime.getStyleClass().add("triplisttime");
			leaveTime.setAlignment(Pos.CENTER_LEFT);
			startPane.getChildren().add(leaveTime);
			
			infoPane.getChildren().add(startPane);
			
			// Arrow
			ImageView arrow = new ImageView("arrow_forward.png");
			arrow.setFitWidth(20);
			arrow.setPreserveRatio(true);
			arrow.setCache(true);
			infoPane.getChildren().add(arrow);
			
			// Destination location info
			VBox destPane = new VBox();
			destPane.setPrefWidth(150);
			
			Label destName = new Label(trip.getDest());
			destName.getStyleClass().add("triplistname");
			destName.setAlignment(Pos.CENTER_RIGHT);
			destName.setTextAlignment(TextAlignment.RIGHT);
			destPane.getChildren().add(destName);
			
			HBox destWeatherPane = new HBox();
			destWeatherPane.setAlignment(Pos.CENTER);
			
			Label destWeatherIcon = new Label(
					WeatherApp.weatherIconMap[destWeather.getCondCode()]);
			destWeatherIcon.getStyleClass().add("weathericon");
			destWeatherIcon.setId("triplistwicon");
			destWeatherIcon.setPrefWidth(75);
			destWeatherIcon.setTranslateY(-10);
			destWeatherIcon.setAlignment(Pos.CENTER);
			destWeatherPane.getChildren().add(destWeatherIcon);
			
			Label destTemp = new Label(destWeather.getCurrentTemp() + "\u2103");
			destTemp.getStyleClass().add("triplisttemp");
			destTemp.setPrefWidth(75);
			destTemp.setAlignment(Pos.CENTER);
			destWeatherPane.getChildren().add(destTemp);
			
			destPane.getChildren().add(destWeatherPane);
			
			Label arriveTime = new Label("Arrive " + trip.getArriveTime());
			arriveTime.getStyleClass().add("triplisttime");
			arriveTime.setAlignment(Pos.CENTER_RIGHT);
			arriveTime.setTextAlignment(TextAlignment.RIGHT);
			destPane.getChildren().add(arriveTime);
			
			infoPane.getChildren().add(destPane);
		}
		
		public HBox getPane() {
			return infoPane;
		}
	}

	public PgTriplist() {
		super("triplist", "Trip List", "Back", "Clothing Suggestion");
	}
	
	@Override
	void createContent() {
		// Set up the scroll pane
		scrollPane = new ScrollPane();
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setFitToWidth(true);
		scrollPane.setPrefSize(Page.BTN_WIDTH, Page.BTN_HEIGHT * 4);
		
		// Set up and create the content in the scroll pane
		scrollContent = new VBox();
		scrollContent.setPrefWidth(Page.BTN_WIDTH);
		
		for(Trip trip : WeatherApp.trips) {
			// Display only trips for this day
			if(trip.getRepeat()[WeatherApp.currentlyViewingDay]) {
				Button tripBtn = new Button();
				tripBtn.setGraphic((new TriplistPane(trip)).getPane());
		        tripBtn.setPrefSize(Page.BTN_WIDTH, Page.BTN_HEIGHT);
		        tripBtn.setStyle("-fx-background-color: " + WeatherApp.
		        		colorMap[WeatherApp.currentlyViewingDay]);
		        tripBtn.setBorder(new Border(new BorderStroke(
						Color.web("#7d7d7d"), 
						BorderStrokeStyle.SOLID,
						CornerRadii.EMPTY,
						new BorderWidths(0, 0, 1, 0))));
		        tripBtn.setOnAction(e -> {
		        	WeatherApp.currentlyViewingTrip = trip;
		        	WeatherApp.changePage("tripdetail");
		        });
		        scrollContent.getChildren().add(tripBtn);
			}
		}
		
        scrollPane.setContent(scrollContent);
        mainContentGrid.getChildren().add(scrollPane);
	}


	@Override
	void leftButtonAction() {
		WeatherApp.changePage("overview");
	}

	@Override
	void rightButtonAction() {
		WeatherApp.changePage("clothsuggest");
	}

}
