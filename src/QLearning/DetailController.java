package QLearning;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Dong Yubo
 */
public class DetailController implements Initializable {

    @FXML
    private StackPane locationPane;

    @FXML
    private VBox detailPane;

    @FXML
    private Label label;

    public StackPane getLocationPane() {
        return locationPane;
    }

    public VBox getDetailPane() {
        return detailPane;
    }

    public Label getLabel() {
        return label;
    }

    public void setTimeLabel(double[] time, boolean isBlock) {
        if (isBlock) {
            upTimeLabel.setText(null);
            downTimeLabel.setText(null);
            leftTimeLabel.setText(null);
            rightTimeLabel.setText(null);
        } else {
            Format form = new DecimalFormat("0.##");
            upTimeLabel.setText(form.format(time[GridWorld.Up]));
            downTimeLabel.setText(form.format(time[GridWorld.Down]));
            leftTimeLabel.setText(form.format(time[GridWorld.Left]));
            rightTimeLabel.setText(form.format(time[GridWorld.Right]));
        }
    }

    public void resetTimeLabel() {
        upTimeLabel.setText(null);
        downTimeLabel.setText(null);
        leftTimeLabel.setText(null);
        rightTimeLabel.setText(null);
    }

    @FXML
    private StackPane locationTimePane;

    @FXML
    private Label downTimeLabel;

    @FXML
    private Label upTimeLabel;

    @FXML
    private Label leftTimeLabel;

    @FXML
    private Label rightTimeLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        assert locationPane != null : "fx:id=\"location\" was not injected: check your FXML file 'detail.fxml'.";
        assert detailPane != null : "fx:id=\"detail\" was not injected: check your FXML file 'detail.fxml'.";
        assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'detail.fxml'.";
        label.setText(null);
        resetTimeLabel();
    }

}
