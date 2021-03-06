package application;

import java.io.IOException;
import javafx.animation.*;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
import eu.hansolo.medusa.Gauge;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
	private Gauge throttleGauge;
	@FXML
	private Gauge fuelGauge;
	@FXML
	private Label gear;
	@FXML
	private Circle g1,g2,y1,y2,r1,r2; //Annunciator "lights"
	

	private CommandControl control;
	private CarData data;

	public MainDashController() throws IOException, InterruptedException {
		data = new CarData();

	}

	private Service<Void> obdControl;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		gear.setText("P");
		
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
					g1.setFill(Color.web("0x4DFF00"));
					g2.setFill(Color.web("0x4DFF00"));
					
					
					if (rpm > 3000) {
						y1.setFill(Color.web("0xFBFF00"));
						y2.setFill(Color.web("0xFBFF00"));

						if (rpm > 4000) {
							r1.setFill(Color.web("0xFF0000"));
							r2.setFill(Color.web("0xFF0000"));
						}else{
							r1.setFill(Color.web("0x700d0b"));
							r2.setFill(Color.web("0x700d0b"));
						}
					}else{
						y1.setFill(Color.web("0x82860b"));
						y2.setFill(Color.web("0x82860b"));
					}
				}else{
					g1.setFill(Color.web("0x0b7215"));
					g2.setFill(Color.web("0x0b7215"));
				}
				mphGauge.setValue(data.getMph());
				tempGauge.setValue(data.getCoolandTemp());
				throttleGauge.setValue(data.getThrottlePos());
				fuelGauge.setValue(data.getFuelLevel());
				gear.setText(data.getGear());
			}
		}), new KeyFrame(Duration.seconds(0.03)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

}