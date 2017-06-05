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

	private CommandControl control;
	private CarData data;

	public MainDashController() throws IOException, InterruptedException {
		data = new CarData();

	}

	private Service<Void> obdControl;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

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
	}

	@FXML
	private void bindToTime() {
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				rpmGauge.setValue(data.getRpm());
				mphGauge.setValue(data.getMph());
				tempGauge.setValue(data.getCoolandTemp());
				throttleGauge.setValue(data.getThrottlePos());
				fuelGauge.setValue(data.getFuelLevel());
			}
		}), new KeyFrame(Duration.seconds(0.03)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

}