package application;

import java.io.IOException;
import javafx.animation.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class MainDashController implements Initializable {
    @FXML
    private Button mph;
    @FXML
    private Button rpm;
    @FXML
    private Button throttlePos;
    @FXML
    private Button coolantTemp;
    @FXML
    private Button fuelLevel;
    
    private CommandControl control;
    private CarData data;
    
    public MainDashController() throws IOException, InterruptedException{
        data = new CarData();
        control = new CommandControl(data);
        control.run();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mph.setText("" + data.getMph());
        rpm.setText("" + data.getMph());
        throttlePos.setText("" + data.getMph());
        coolantTemp.setText("" + data.getMph());
        fuelLevel.setText("" + data.getMph());
    }    
    
    private void bindToTime() {
  Timeline timeline = new Timeline(
    new KeyFrame(Duration.seconds(0),
      new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent actionEvent) {
          mph.setText("" + data.getMph());
          rpm.setText("" + data.getMph());
          throttlePos.setText("" + data.getMph());
          coolantTemp.setText("" + data.getMph());
          fuelLevel.setText("" + data.getMph());
        }
      }
    ),
    new KeyFrame(Duration.seconds(0.1)));
  timeline.setCycleCount(Animation.INDEFINITE);
  timeline.play();
 }
}