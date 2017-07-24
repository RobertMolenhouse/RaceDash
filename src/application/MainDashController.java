package application;

import java.io.IOException;
import javafx.animation.*;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

import com.sun.glass.events.TouchEvent;

import eu.hansolo.medusa.Gauge;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MainDashController implements Initializable {
	@FXML
	private Gauge rpmGauge;
	@FXML
	private Gauge mphGauge;
	@FXML
	private Gauge tempGauge;
	@FXML 
	private Gauge oilTemp;
	@FXML
	private Gauge throttleGauge;
	@FXML
	private Gauge fuelGauge;
	@FXML
	private Label gear;
	@FXML
	private Pane ALPane;
	@FXML
	private Circle g1,g2,y1,y2,r1,r2; //Annunciator "lights"
	@FXML
	private Button exitButton;
	

	private CommandControl control;
	private CarData data;

	public MainDashController() throws IOException, InterruptedException {
		data = new CarData();

	}

	private Service<Void> obdControl;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		gear.setText("P");
		exitButton.setOnAction(e -> Platform.exit());
		
		bindToTime();

		obdControl = new Service<Void>() {

			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						try {
							control = new CommandControl(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							control.run();
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;

					}
				};
			}
		};
		obdControl.start();
	}

	@FXML
	private void bindToTime() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				int rpm = data.getRpm();
				rpmGauge.setValue(rpm);
				
				//check for annunciator lights.
				if (rpm > 2000) {
					g1.setFill(Color.web("0x3FFF2F"));
					g1.setOpacity(1);
					g2.setFill(Color.web("0x3FFF2F"));
					g2.setOpacity(1);
					if (rpm > 3000) {
						y1.setFill(Color.web("0xFBFF00"));
						y1.setOpacity(1);
						y2.setFill(Color.web("0xFBFF00"));
						y2.setOpacity(1);
						if (rpm > 4000) {
							r1.setFill(Color.web("0xFF0000"));
							r1.setOpacity(1);
							r2.setFill(Color.web("0xFF0000"));
							r2.setOpacity(1);
						}else{
							r1.setFill(Color.web("0x700d0b"));
							r1.setOpacity(0.25);
							r2.setFill(Color.web("0x700d0b"));
							r2.setOpacity(0.25);
						}
					}else{
						y1.setFill(Color.web("0x82860b"));
						y1.setOpacity(0.25);
						y2.setFill(Color.web("0x82860b"));
						y2.setOpacity(0.25);
					}
				}else{
					g1.setFill(Color.web("0x0b7215"));
					g1.setOpacity(0.25);
					g2.setFill(Color.web("0x0b7215"));
					g2.setOpacity(0.25);
				}
				mphGauge.setValue(data.getMph());
				tempGauge.setValue(data.getCoolandTemp());
				throttleGauge.setValue(data.getThrottlePos());
				fuelGauge.setValue(data.getFuelLevel());
				oilTemp.setValue(data.getOilTemp());
				gear.setText(data.getGear());
			}
		}), new KeyFrame(Duration.seconds(0.03)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
	
	@FXML
	private void ALHandler(ActionEvent event){
		
	}

}