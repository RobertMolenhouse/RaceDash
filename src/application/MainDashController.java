package application;

import java.io.IOException;
import javafx.animation.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import eu.hansolo.medusa.FGauge;
import eu.hansolo.medusa.FGaugeBuilder;
import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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
    
    public MainDashController() throws IOException, InterruptedException{
    	data = new CarData();
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
		rpmGauge.setValue(data.getRpm());
		mphGauge.setValue(data.getMph());
		tempGauge.setValue(data.getCoolandTemp());
		throttleGauge.setValue(data.getThrottlePos());
		fuelGauge.setValue(data.getFuelLevel());

        bindToTime();
        
        try {
			control = new CommandControl(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			control.start();
		}catch(Exception e){
			e.printStackTrace();
		}
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